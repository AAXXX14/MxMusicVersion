package com.lq.mxmusic.view.activity

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.view.ViewPager
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.lq.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.AppConfig.NEEDLE_BACK_ANIMATOR_DURATION
import com.lq.mxmusic.reposity.config.AppConfig.NEEDLE_GO_ANIMATION_DURATION
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.entity.CurrentMusicEntity
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.callback.SafeClickCallBack
import com.lq.mxmusic.reposity.config.AppConfig.ROTATE_ANIMATOR_DURATION
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.service.MusicPlayService
import com.lq.mxmusic.util.LogUtil
import com.lq.mxmusic.util.PlayUtils
import com.lq.mxmusic.util.ScanMusicUtils
import com.lq.mxmusic.util.StatusBarUtil
import com.lq.mxmusic.view.adapter.PlayDiscViewPagerAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music_play.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/*
*2018/10/12 0012  11:22
*播放 by lq
*/
class MusicPlayActivity : BaseActivity() {
    private var eventChange = false
    private var selectedPosition = 0
    private val mNeedleBackAnimation by lazy {
        //指针 返回 动画
        RotateAnimation(-20f, 0f, Animation.RELATIVE_TO_SELF, 0.33f, Animation.RELATIVE_TO_SELF, 0.13f).apply {
            duration = NEEDLE_BACK_ANIMATOR_DURATION
            interpolator = AccelerateInterpolator()
        } as Animation
    }
    private val mNeedleGoAnimation by lazy {
        //指针 摇摆动画
        RotateAnimation(0f, -20f, Animation.RELATIVE_TO_SELF, 0.33f, Animation.RELATIVE_TO_SELF, 0.13f).apply {
            duration = NEEDLE_GO_ANIMATION_DURATION
            interpolator = LinearInterpolator()
        } as Animation
    }
    private val mPlayTimeMap by lazy { SparseIntArray() }
    private val mDiscLayouts by lazy { ArrayList<View>() }
    private val mAnimationList by lazy { ArrayList<ObjectAnimator>() }
    private val playList by lazy { ArrayList<LocalMusicEntity>() } //当前播放的列表  的 图片
    private val musicAllList by lazy { ArrayList<CurrentMusicEntity>() }//全部播放列表
    private val adapter by lazy { PlayDiscViewPagerAdapter(mDiscLayouts) }
    private lateinit var mServiceBinder: MusicPlayService.PlayBinder
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            //与service 的连接 意外中断
            ShowUtils.showInfo(this@MusicPlayActivity, "连接中断")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mServiceBinder = service as MusicPlayService.PlayBinder
            LogUtil.i("Connected","yes i connected it")
            //service 连接成功
            initListener()
        }

    }


    //将当前 列表 添加到 CurrentPlayList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)
        adapterStatus()
        initData()
        initViewPagerData()
        judgeState()
        toBindService()
    }

    private fun toBindService() {
        //通过绑定service 将service 中的mediaPlay 与seekBar 绑定监听
        val serviceIntent = Intent(this, MusicPlayService::class.java)
        val canBind = bindService(serviceIntent, object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                //与service 的连接 意外中断
                ShowUtils.showInfo(this@MusicPlayActivity, "连接中断")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mServiceBinder = service as MusicPlayService.PlayBinder
                LogUtil.i("Connected","yes i connected it")
                //service 连接成功
                initListener()
            }

        }, Context.BIND_AUTO_CREATE)
        LogUtil.i("PlayBinder","$canBind")
    }

    private fun judgeState() {
        if (PlayConfig.IS_PLAY) {
            //播放状态 指针回摆
            stopNeedleAnimation()
            //旋转动画
            startRecordRotation()
        } else {
            //暂停状态 指针离开
            startNeedleAnimation()
            //停止旋转动画
            stopRecordRotation()
        }
        forbidShowBottom()
    }


    private fun adapterStatus() {
        val height = StatusBarUtil.getStatusBarHeight(this)
        val params = musicPlayLl.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = height
        musicPlayLl.layoutParams = params
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /*开始指针动画*/
    private fun startNeedleAnimation() {
        ivNeedle.run {
            clearAnimation()
            startAnimation(mNeedleGoAnimation)
        }
        mNeedleGoAnimation.fillAfter = true
    }

    /*结束指针动画*/
    private fun stopNeedleAnimation() {
        ivNeedle.run {
            clearAnimation()
            startAnimation(mNeedleBackAnimation)
        }
        mNeedleBackAnimation.fillAfter = true
    }

    /*开始唱片旋转动画*/
    private fun startRecordRotation() {
        if (mAnimationList.size > 0) {
            val currentItem = playViewPager.currentItem
            val animator = mAnimationList[currentItem]
            animator.start()
            if (mPlayTimeMap.size() > 0 && mPlayTimeMap.size() > currentItem) {
                val playTime = mPlayTimeMap[currentItem]
                animator.currentPlayTime = playTime.toLong()
            }
        }
    }

    /*停止唱片旋转动画*/
    private fun stopRecordRotation() {
        if (playViewPager != null && mAnimationList.size > playViewPager.currentItem) {
            val animator = mAnimationList[playViewPager.currentItem]
            //获取播放的时长
            val mCurrentPlayDate = animator.currentPlayTime.toInt()
            mPlayTimeMap.put(playViewPager.currentItem, mCurrentPlayDate)
            animator.cancel()
        }
    }

    //初始化ViewPager所需数据
    private fun initViewPagerData() {
        mDiscLayouts.clear()
        for (i in playList.indices) {
            val discLayout = LayoutInflater.from(this).inflate(R.layout.layout_disc, playViewPager, false)
            val mMusicPicIV = discLayout.findViewById<ImageView>(R.id.ivDisc)
            Glide.with(this).load(R.drawable.default_bg).placeholder(R.drawable.play_default).error(R.drawable.play_default).into(mMusicPicIV)
            mAnimationList.add(getAnimation(mMusicPicIV))
            mDiscLayouts.add(discLayout)
        }
        playViewPager.adapter = adapter
    }

    //设置动画旋转
    private fun getAnimation(mImageView: View): ObjectAnimator {
        val mDiscAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f)
        mDiscAnimator.duration = ROTATE_ANIMATOR_DURATION
        mDiscAnimator.interpolator = LinearInterpolator()
        mDiscAnimator.repeatCount = Animation.INFINITE
        return mDiscAnimator
    }

    private fun initData() {
        setToolbarColor(R.color.transparent)
        //设置副标题主标题
        forbidShowToolbar()
        forbidShowBottom()
        val playSource = intent.getIntExtra(AppConfig.PLAY_SOURCE, 1)//默认是本地的
        when (playSource) {
            AppConfig.PLAY_LOCAL -> {//本地播放  从intent传递过来的数据
                val entity = intent.getParcelableExtra<LocalMusicEntity>(AppConfig.PLAY_ENTITY)
                //todo 标识当前播放列表为 本地播放
                if (entity != null) {
                    val playName = entity.musicName
                    val singerName = entity.musicSingerName
                    playNameTv.text = playName
                    singerNameTv.text = singerName
                    val progress = entity.musicProgress
                    musicPlaySeekBar.max = entity.musicLength.toInt()
                    musicLengthTv.text = ScanMusicUtils.obtainStringTime(entity.musicLength)
                } else {//从数据库找
                    val position = SharedPreferencesUtil.getPlayPosition()
                    val list = AppDataBase.instance.localMusicDao().queryAll()
                    val bean = list[position]
                    val playName = bean.musicName
                    val singerName = bean.musicSingerName
                    playNameTv.text = playName
                    singerNameTv.text = singerName
                }
                if (AppConfig.localPlayList!!.size > 1) {
                    playList.add(AppConfig.localPlayList!![0])//添加第一项
                    playList.add(AppConfig.localPlayList!![AppConfig.localPlayList!!.size - 1])//添加最后一项
                }
                playList.addAll(AppConfig.localPlayList!!)
                adapter.notifyDataSetChanged()
            }
            AppConfig.PLAY_NEARLY -> {//最近播放
            }
        }
        Glide.with(this).load(R.drawable.default_bg).placeholder(R.drawable.play_default).bitmapTransform(BlurTransformation(this, 25, 8)).into(playBgIv)
    }

    private fun initListener() {
        play_back.setOnClickListener(object : SafeClickCallBack() {
            override fun onNoDoubleClick(v: View) {
                onBackPressed()
            }
        })
        playViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //页面状态改变。当按下时不需要改变，滑动动画完成时不需要改变，但触摸离开屏幕时要清除动画
                when (state) {
                    ViewPager.SCROLL_STATE_IDLE -> {
                    }//滑动动画完成时启用的方法
                    ViewPager.SCROLL_STATE_SETTLING -> stopNeedleAnimation()//离开屏幕时启用
                    ViewPager.SCROLL_STATE_DRAGGING -> startNeedleAnimation()//按住屏幕开始拖拽时启用
                }
            }

            override fun onPageScrolled(position: Int, p1: Float, p2: Int) {
                selectedPosition = position
            }

            override fun onPageSelected(p0: Int) {
                if (!eventChange) {  //自动通知
                    if (p0 != musicAllList.size - 1) {
                        if (p0 > selectedPosition) {
                            //下一首
                            PlayUtils.next()
                        } else {
                            //上一首
                            PlayUtils.previous()
                        }
                    } else {//为最后一项的时候 进入到第一项
                        eventChange = true
                        SharedPreferencesUtil.setPlayPosition(0)
                        PlayUtils.play()
                        playViewPager.setCurrentItem(1, false)
                        eventChange = false
                    }
                    if (p0 == 0) {//为第一项的时候 进入最后一项
                        eventChange = true
                        SharedPreferencesUtil.setPlayPosition(musicAllList.size - 2)
                        PlayUtils.play()
                        playViewPager.setCurrentItem(musicAllList.size - 2, true)
                        eventChange = false
                    }
                }
            }
        })
        musicPlaySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //拖动  获取当前播放进度
                val mProgress = seekBar!!.progress
                mServiceBinder.playSeekTo(mProgress)
                LogUtil.i("Progress", "$mProgress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun PlayStateChangeEvent(event: MusicPlayServiceEvent.MusicPlayChangeStateEvent) {
        if (PlayConfig.IS_PLAY) {
            if (playViewPager != null) {
                val list = AppConfig.localPlayList!!
                playList.clear()
                playList.add(list[list.size - 1])//添加最后一项
                playList.addAll(list)
                playList.add(list[0])//添加第一项
                adapter.notifyDataSetChanged()
                eventChange = true //不允许viewPager 切换播放

                val position = SharedPreferencesUtil.getPlayPosition() + 1
                playViewPager.setCurrentItem(position, false)
                eventChange = false

                val entity = list[position + 1]
                val playName = entity.musicName
                val singerName = entity.musicSingerName
                playNameTv.text = playName
                singerNameTv.text = singerName
                mPlayTimeMap.clear()
                startRecordRotation()
            } else {
                stopRecordRotation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        unbindService(serviceConnection)
    }
}