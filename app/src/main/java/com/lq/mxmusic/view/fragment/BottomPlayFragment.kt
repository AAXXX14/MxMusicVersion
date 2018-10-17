package com.lq.mxmusic.view.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.util.LogUtil
import com.lq.mxmusic.util.PlayUtils
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
        if (AppConfig.localPlayList != null) {
            allList.addAll(AppConfig.localPlayList!!)
            allList.add(AppConfig.localPlayList!![0])
        }
        adapter.setData(allList)
        bottomContainerViewPager.adapter = adapter
    }

    private fun initListener() {
        bottomContainerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectedPosition = position
            }

            override fun onPageSelected(p0: Int) {
                if (!eventChange) {
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
                        AppConfig.currentPlayPosition = 0
                        PlayUtils.play()
                        bottomContainerViewPager.setCurrentItem(0, false)
                        eventChange = false
                    }
                    if(p0== 0){//为第一项的时候 进入最后一项
                        eventChange = true
                        AppConfig.currentPlayPosition = allList.size -2
                        PlayUtils.play()
                        bottomContainerViewPager.setCurrentItem(allList.size-2, false)
                        eventChange = false
                    }
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun changeState(event: MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent) {
        if (PlayConfig.IS_PLAY) {
            val list = AppConfig.localPlayList!!
            allList.clear()
            allList.add(list[list.size-1])//添加最后一项
            allList.addAll(list)
            allList.add(list[0])//添加第一项

            adapter.setData(allList)
            eventChange = true
            val position = AppConfig.currentPlayPosition
            LogUtil.i("Position", "$position")
            LogUtil.w("Size", "${allList.size - 1}")
            bottomContainerViewPager.setCurrentItem(position,false)
            eventChange = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}