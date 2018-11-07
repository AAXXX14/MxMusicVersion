package com.lq.mxmusic.service

import android.app.Service
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.util.LogUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/*
*2018/10/12 0012  11:27
*播放 by lq
*/
class MusicPlayService : Service() {
    private val TAG = "MusicPlayService"
    private var status = 3         //播放状态，默认为顺序播放
    private var currentTime: Int = 0        //当前播放进度

    companion object {
        private var current = 0        // 记录当前正在播放的音乐 position
        private val musicInfoList = ArrayList<LocalMusicEntity>()   //存放Mp3Info对象的集合
        private var path: String? = null            // 音乐文件路径
        val mediaPlayer by lazy { MediaPlayer() }

        /*设置播放列表*/
        fun setData(list: List<LocalMusicEntity>) {
            musicInfoList.clear()
            musicInfoList.addAll(list)
            //存储变量当前播放列表
            AppConfig.localPlayList = musicInfoList
            current = SharedPreferencesUtil.getPlayPosition()
            if (musicInfoList.size > current)
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
    fun serviceStateChangeEvent(event: MusicPlayServiceEvent.MusicPlayServiceChangeStateEvent) {
        when (PlayConfig.CURRENT_STATE) {
            PlayConfig.PLAY -> play(0)
            PlayConfig.NEXT -> next()
            PlayConfig.PREVIOUS -> previous()
            PlayConfig.PAUSE -> pause()
            PlayConfig.RESUME -> resume()
        }
    }

    /*播放*/
    private fun play(currentTime: Int) {
        this.currentTime = currentTime
        mediaPlayer.reset()// 把各项参数恢复到初始状态
        current = SharedPreferencesUtil.getPlayPosition()
        path = musicInfoList[current].musicPath
        mediaPlayer.setDataSource(path)
        SharedPreferencesUtil.setPlayPosition(current)
        EventBus.getDefault().postSticky(MusicPlayServiceEvent.MusicPlayChangeStateEvent)
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

    /*暂停音乐*/
    private fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        PlayConfig.IS_PLAY = false
        PlayConfig.CURRENT_STATE = PlayConfig.PAUSE
        //存储当前播放进度
        SharedPreferencesUtil.setCurrentPlayPosition(mediaPlayer.currentPosition)
        //通知播放页面进行动画操作，进行数据更换。
    }

    /*恢复0*/
    private fun resume() {
        currentTime = SharedPreferencesUtil.getCurrentPlayPosition()
        play(currentTime)
        PlayConfig.IS_PLAY = true
    }

    /*初始刷监听*/
    private fun initListener() {
        mediaPlayer.setOnCompletionListener {
            when (status) {
                1 -> { mediaPlayer.start()}// 单曲循环
                2 -> { // 全部循环
                    current++
                    if (current > musicInfoList.size - 1) {  //变为第一首的位置继续播放
                        current = 0
                    }
                    SharedPreferencesUtil.setPlayPosition(current)
                    play(0)
                }
                3 -> {//顺序播放
                    current++
                    if (current <= musicInfoList.size - 1) {
                        SharedPreferencesUtil.setPlayPosition(current)
                    }else{
                        SharedPreferencesUtil.setPlayPosition(0)
                    }
                    play(0)
                }
                4 -> {    //随机播放
                    val position= Random().nextInt(musicInfoList.size)
                    SharedPreferencesUtil.setPlayPosition(position)
                    play(0)
                }
            }
        }
        mediaPlayer.setOnPreparedListener {
            it.start() // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                it.seekTo(currentTime)
            }
            //往最近播放中 添加数据
            if (path == null) {
                //播放路径错误  不添加进表单
            } else {
                val entity = AppDataBase.instance.nearlyMusicDao().queryByPath(path!!)
                if (entity != null) { //先删除 再添加 将本条目 置顶
                    AppDataBase.instance.nearlyMusicDao().deleteMusic(entity)
                }
                val data = musicInfoList[current]
                val nearlyEntity = NearlyMusicEntity(data.id, data.musicName, data.musicSingerName, data.musicPath, data.musicProgress, data.musicLength,"")
//                AppDataBase.instance.nearlyMusicDao().insertMusic(nearlyEntity)
            }
        }
    }

    class PlayBinder :Binder(){
        //跟进
        fun playSeekTo(progress:Int){
            mediaPlayer.seekTo(progress)
        }

        fun playProgress() :Int{
            return mediaPlayer.currentPosition
        }
    }

    class PlayServiceViewModel:ViewModel(){
        val mediaProgress by lazy { MutableLiveData<Int>() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
        EventBus.getDefault().unregister(this)
    }

}