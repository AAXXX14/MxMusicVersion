package com.lq.mxmusic.service

import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import java.util.ArrayList

/*
*2018/10/12 0012  11:27
*播放 by lq
*/
class MusicPlayService : Service() {
    private val TAG = "MusicPlayService"
    private var mediaPlayer:MediaPlayer ? = null
    private var path: String? = null            // 音乐文件路径
    private var isPause: Boolean = false        // 暂停状态
    private var current = 0        // 记录当前正在播放的音乐
    private val musicInfoList = ArrayList<LocalMusicEntity>()   //存放Mp3Info对象的集合
    private var status = 3         //播放状态，默认为顺序播放
    private var currentTime: Int = 0        //当前播放进度
    private val duration: Int = 0           //播放长度


    //服务要发送的一些Action
    val UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION"  //更新动作
    val CTL_ACTION = "com.wwj.action.CTL_ACTION"        //控制动作
    val MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"  //当前音乐播放时间更新动作
    val MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION"//新音乐长度更新动作

    //控制音乐播放
    val MUSIC_PLAY = "music_play"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}