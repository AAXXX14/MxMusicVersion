package com.lq.mxmusic.util

import android.content.Context
import android.content.Intent
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.service.MusicPlayService
import com.lq.mxmusic.view.activity.MusicPlayActivity
import org.greenrobot.eventbus.EventBus

/*
*2018/10/17 0017  10:50
*function by lq
*/
object PlayUtils{

    fun preparePlay(context:Context,position:Int,mList:List<LocalMusicEntity>){
        // 开启服务
        if(!ServiceUtil.isRunning("com.lq.mxmusic.service.MusicPlayService")){
            context.startService(Intent(context, MusicPlayService::class.java))
        }
        /*设置播放状态*/
        PlayConfig.CURRENT_STATE = PlayConfig.PLAY
        PlayConfig.IS_PLAY = true
        AppConfig.currentPlayPosition = position
        MusicPlayService.setData(mList,position)
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
        if(AppConfig.currentPlayPosition == position)
            context.startActivity(Intent(context, MusicPlayActivity::class.java)
                    .putExtra(AppConfig.PLAY_ENTITY, mList[position]).putExtra(AppConfig.PLAY_SOURCE, AppConfig.PLAY_LOCAL))
    }

    fun play(){
        PlayConfig.CURRENT_STATE = PlayConfig.PLAY
        PlayConfig.IS_PLAY = true
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun next(){
        AppConfig.currentPlayPosition++
        PlayConfig.CURRENT_STATE = PlayConfig.NEXT
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }

    fun previous(){
        AppConfig.currentPlayPosition--
        PlayConfig.CURRENT_STATE = PlayConfig.PREVIOUS
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent)
    }
}