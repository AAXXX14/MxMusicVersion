package com.lq.mxmusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.util.LogUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/*
*2018/10/12 0012  11:27
*播放 by lq
*/
class MusicPlayService : Service() {
    private val TAG = "MusicPlayService"
    private val mediaPlayer by lazy { MediaPlayer() }
    private var status = 3         //播放状态，默认为顺序播放
    private var currentTime: Int = 0        //当前播放进度

    companion object {
        private var current = 0        // 记录当前正在播放的音乐 position
        private val musicInfoList = ArrayList<LocalMusicEntity>()   //存放Mp3Info对象的集合
        private var path: String? = null            // 音乐文件路径
        /*设置播放列表*/
        fun setData(list: List<LocalMusicEntity>, current: Int) {
            musicInfoList.clear()
            musicInfoList.addAll(list)
            //存储变量当前播放列表
            AppConfig.localPlayList = musicInfoList
            this.current = current
            path = musicInfoList[current].musicPath
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initListener()
        EventBus.getDefault().register(this)
    }

    /*更新当前状态*/
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun changeState(event: MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent) {
        when (PlayConfig.CURRENT_STATE) {
            PlayConfig.PLAY -> play(0)
            PlayConfig.NEXT -> next()
            PlayConfig.PREVIOUS -> previous()
            PlayConfig.PAUSE -> pause()
            PlayConfig.RESUME -> resume()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun setData(event: MusicPlayServiceEvent) {

    }

    /*播放*/
    private fun play(currentTime: Int) {
        this.currentTime = currentTime
        mediaPlayer.reset()// 把各项参数恢复到初始状态
        path = musicInfoList[current].musicPath
        LogUtil.w("LogTag", "$current")
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync() // 进行缓冲

        PlayConfig.IS_PLAY = true
        PlayConfig.CURRENT_STATE = PlayConfig.PLAY
    }

    /*下一首*/
    private fun next() {
        if (current + 1 < musicInfoList.size) {
            current++
        } else {
            current = 0
        }
        path = musicInfoList[current].musicPath
        play(0)
    }

    /*上一首*/
    private fun previous() {
        if (current - 1 >= 0) {
            current--
        } else {
            current = musicInfoList.size - 1
        }
        path = musicInfoList[current].musicPath
        play(0)
    }

    /**
     * 暂停音乐
     */
    private fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            PlayConfig.IS_PLAY = false
            PlayConfig.CURRENT_STATE = PlayConfig.PAUSE
            //存储当前播放进度
            SharedPreferencesUtil.setCurrentPlayPosition(currentTime)
            //通知播放页面进行动画操作，进行数据更换。
        }
    }

    /*恢复0*/
    private fun resume() {
        currentTime = SharedPreferencesUtil.getCurrentPlayPosition()
        play(currentTime)
    }

    private fun initListener() {
        mediaPlayer.setOnCompletionListener {
            if (status == 1) { // 单曲循环
                mediaPlayer.start()
            } else if (status == 2) { // 全部循环
                current++
                if (current > musicInfoList.size - 1) {  //变为第一首的位置继续播放
                    current = 0
                }
                play(0)
            } else if (status == 3) {//顺序播放
                current++
                if (current <= musicInfoList.size - 1) {
                    play(0)
                } else {
                    //nothing to do
                }
            } else if (status == 4) {    //随机播放

            }
        }
        mediaPlayer.setOnPreparedListener {
            it.start() // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                it.seekTo(currentTime)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}