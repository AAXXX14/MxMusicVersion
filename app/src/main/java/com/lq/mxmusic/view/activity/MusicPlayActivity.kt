package com.lq.mxmusic.view.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
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
import com.lq.mxmusic.view.adapter.PlayDiscViewPagerAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music_play.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/*
*2018/10/12 0012  11:22
*播放 by lq
*/
class MusicPlayActivity : BaseActivity() {
    private val playList = ArrayList<String>()//当前播放的列表  的 图片
    private val adapter = PlayDiscViewPagerAdapter(playList)
    private val musicAllList = ArrayList<CurrentMusicEntity>()//全部播放列表
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


    //将当前 列表 添加到 CurrentPlayList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)

        initData()
        initListener()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun changeState(event: MusicPlayServiceEvent.MusicPlayChangeStateEvent) {
        if (PlayConfig.IS_PLAY) {
            //播放状态 指针回摆
            stopNeedleAnimation()
            //旋转动画
            startRecordRotation()
        } else {
            //暂停状态 指针离开
            startNeedleAnimation()
            stopRecordRotation()
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
        if(adapter.mAnimationList.size>0){
            val animator = adapter.mAnimationList[playViewPager.currentItem]
            animator.start()
        }
    }

    /*停止唱片旋转动画*/
    private fun stopRecordRotation() {
      if(adapter.mAnimationList.size>playViewPager.currentItem){
          val animator = adapter.mAnimationList[playViewPager.currentItem]
          animator.cancel()
      }
    }

    private fun initData() {
        setFitSystem(false)
        setStatusColor(R.color.transparent)
        setToolbarColor(R.color.transparent)
        //设置副标题主标题
        forbidShowToolbar()
        forbidShowBottom()
        playViewPager.adapter = adapter
        val playSource = intent.getIntExtra(AppConfig.PLAY_SOURCE, 1)//默认是本地的
        when (playSource) {
            AppConfig.PLAY_LOCAL -> {//本地播放
                val entity = intent.getParcelableExtra<LocalMusicEntity>(AppConfig.PLAY_ENTITY)
                //todo 标识当前播放列表为 本地播放
                if (entity != null) {
                    val playName = entity.musicName
                    val singerName = entity.musicSingerName
                    playNameTv.text = playName
                    singerNameTv.text = singerName
                }
                //todo 通知播放
            }
        }
        playList.add("")
        playList.add("")
        playList.add("")
        playList.add("")
        playList.add("")
        playList.add("")
        playList.add("")
        adapter.notifyDataSetChanged()
        Glide.with(this).load(R.drawable.default_bg).bitmapTransform(BlurTransformation(this, 25, 8)).into(playBgIv)
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

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
            }
        })
    }

    //初始化动画
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}