package com.lq.mxmusic.view.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.service.MusicPlayService
import com.lq.mxmusic.util.LogUtil
import com.lq.mxmusic.util.PlayUtils
import com.lq.mxmusic.callback.SafeClickCallBack
import com.lq.mxmusic.view.adapter.ContainerBtnViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_bottom_play.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/*
*2018/10/16 0016  14:53
*底部播放 fragment by lq
*/
class BottomPlayFragment : BaseFragment() {
    val allList by lazy { ArrayList<LocalMusicEntity>() }
    val adapter by lazy { ContainerBtnViewPagerAdapter() }
    private var selectedPosition = 0
    private var eventChange = false
    override fun setContent(): Int {
        return R.layout.fragment_bottom_play
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initListener()
        EventBus.getDefault().register(this)
    }

    private fun initData() {
        val sourceList = AppConfig.localPlayList
        if (sourceList != null) {
            if (sourceList.size > 1){
                allList.add(sourceList[0])//添加第一项
                allList.add(sourceList[sourceList.size - 1])//添加最后一项
            }
            allList.addAll(sourceList)
        } else {
            val source = SharedPreferencesUtil.getPlaySource()
            when (source) {
                AppConfig.PLAY_LOCAL -> {
                    val list = AppDataBase.instance.localMusicDao().queryAll()
                    allList.clear()
                    allList.addAll(list)
                    MusicPlayService.setData(list)
                }//查询该数据库中数据添加进去
            }
        }
        adapter.setData(allList)
        bottomContainerViewPager.adapter = adapter
        val currentPosition = SharedPreferencesUtil.getPlayPosition()
        if (adapter.count > currentPosition)
            bottomContainerViewPager.setCurrentItem(currentPosition, false)
    }

    private fun initListener() {
        bottomContainerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectedPosition = position
            }

            override fun onPageSelected(p0: Int) {
                if (!eventChange) {  //自动通知
                    if (p0 != allList.size - 1) {
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
                        bottomContainerViewPager.setCurrentItem(1, false)
                        eventChange = false
                    }
                    if (p0 == 0) {//为第一项的时候 进入最后一项
                        eventChange = true
                        SharedPreferencesUtil.setPlayPosition(allList.size - 2)
                        PlayUtils.play()
                        bottomContainerViewPager.setCurrentItem(allList.size - 2, true)
                        eventChange = false
                    }
                }
            }
        })
        playListIv.setOnClickListener(object : SafeClickCallBack() {
            override fun onNoDoubleClick(v: View) {
                //当前歌单 bottomSheetDialog 显示
            }
        })
        controlIv.setOnClickListener(object : SafeClickCallBack() {
            override fun onNoDoubleClick(v: View) {
                //判断当前播放状态
                if (PlayConfig.IS_PLAY) {
                    //播放中   暂停
                    PlayUtils.pause()
                } else {
                    //暂停中 播放 当前曲目
                    PlayUtils.resume()
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun changeState(event: MusicPlayServiceEvent.MusicPlayChangeStateEvent) {
        if (PlayConfig.IS_PLAY) {
            val list = AppConfig.localPlayList!!
            allList.clear()
            allList.add(list[list.size - 1])//添加最后一项
            allList.addAll(list)
            allList.add(list[0])//添加第一项

            adapter.setData(allList)
            eventChange = true //不允许viewPager 切换播放
            val position = SharedPreferencesUtil.getPlayPosition() + 1
            LogUtil.i("Position", "$position")
            LogUtil.w("Size", "${allList.size - 1}")
            bottomContainerViewPager.setCurrentItem(position, false)
            eventChange = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}