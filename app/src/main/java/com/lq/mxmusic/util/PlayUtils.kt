package com.lq.mxmusic.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.service.MusicPlayService
import com.lq.mxmusic.view.activity.MusicPlayActivity
import org.greenrobot.eventbus.EventBus

/*
*2018/10/17 0017  10:50
*播放工具 by lq
*/
object PlayUtils {

    fun preparePlay(context: Context, position: Int, source: Int, mList: List<LocalMusicEntity>) {
        val playPosition = SharedPreferencesUtil.getPlayPosition()
        if (playPosition == position) {
            //do nothing
            if (PlayConfig.CURRENT_STATE == PlayConfig.PLAY) {
                context.startActivity(Intent(context, MusicPlayActivity::class.java).run {
                    putExtra(AppConfig.PLAY_ENTITY, mList[position]).putExtra(AppConfig.PLAY_SOURCE, AppConfig.PLAY_LOCAL)
                })
            } else {
                //通知播放
                ServiceUtil.startPlayService(context)
                /*设置播放状态*/
                PlayConfig.CURRENT_STATE = PlayConfig.PLAY
                PlayConfig.IS_PLAY = true
                SharedPreferencesUtil.setPlayPosition(position)
                SharedPreferencesUtil.setPlaySource(source)
                MusicPlayService.setData(mList)
                EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
            }
        } else {
            itemPlay(context, position, source, mList)
        }
    }

    private fun itemPlay(context: Context, position: Int, source: Int, mList: List<LocalMusicEntity>) {
        // 开启服务
        ServiceUtil.startPlayService(context)
        /*设置播放状态*/
        PlayConfig.CURRENT_STATE = PlayConfig.PLAY
        PlayConfig.IS_PLAY = true
        SharedPreferencesUtil.setPlayPosition(position)
        SharedPreferencesUtil.setPlaySource(source)
        MusicPlayService.setData(mList)
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun play() {
        PlayConfig.CURRENT_STATE = PlayConfig.PLAY
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun next() {
        var position = SharedPreferencesUtil.getPlayPosition()
        position++
        SharedPreferencesUtil.setPlayPosition(position)
        PlayConfig.CURRENT_STATE = PlayConfig.NEXT
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun previous() {
        var position = SharedPreferencesUtil.getPlayPosition()
        position--
        SharedPreferencesUtil.setPlayPosition(position)
        PlayConfig.CURRENT_STATE = PlayConfig.PREVIOUS
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun pause() {
        PlayConfig.CURRENT_STATE = PlayConfig.PAUSE
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun resume() {
        PlayConfig.CURRENT_STATE = PlayConfig.RESUME
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }
}