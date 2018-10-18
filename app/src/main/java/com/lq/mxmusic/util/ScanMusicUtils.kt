package com.lq.mxmusic.util

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import com.lq.mxmusic.base.App
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import java.io.IOException
import java.util.ArrayList

/**
 * Created by mx on 2017/8/4.
 * 用于扫描本地歌曲数据   获取播放时间 通过播放时间转换成文本
 */

object ScanMusicUtils {
    private const val TAG = "ScanMusicUtils"

    fun query(mContext: Context): ArrayList<LocalMusicEntity> {
        val arrayList: ArrayList<LocalMusicEntity> = ArrayList()
        //创建一个扫描游标
        val c = mContext.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        if (c != null) {
            while (c.moveToNext()) {
                //扫描本地文件，得到歌曲的相关信息
                val music_name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val music_singer = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))
                val length = obtainMusicTime(path)
                if (length > 15)//音频文件 大于15秒 设定为 歌曲内容
                    arrayList.add(LocalMusicEntity(0, music_name, music_singer, path, 0, length))
            }
        }
        return arrayList
    }

    //获取当前歌曲的时长
    private fun obtainMusicTime(url: String?): Long {
        val mediaPlayer = MediaPlayer()
        var duration = 0
        if (url != null && !url.isEmpty()) {
            LogUtil.e(TAG, url)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            duration = mediaPlayer.duration
            mediaPlayer.release()
        }
        return duration / 1000.toLong() //返回了秒数
    }

    /**
     * 通过存储的总时长转换为String类型数据
     */
    fun obtainStringTime(musicTime: Int): String {
        val minuteTime = musicTime / 60
        var min: String? = null
        min = if (minuteTime / 60 >= 10) {//分钟数
            Integer.toString(minuteTime)
        } else {
            "0$minuteTime"//拼接分钟数
        }
        val surplusSec = musicTime % 60
        var surplus: String? = null//秒数
        surplus = if (surplusSec >= 10) {
            Integer.toString(surplusSec)
        } else {
            "0$surplusSec"
        }
        return "$min:$surplus"
    }

    /**
     * 获取当前歌曲的播放时长
     */
    fun obtainCurrentTime(mediaPlayer: MediaPlayer): String {
        val musicTime = mediaPlayer.currentPosition / 1000//秒数
        val minuteTime = musicTime / 60
        val min = if (minuteTime / 60 >= 10) {//分钟数
            Integer.toString(minuteTime)
        } else {
            "0$minuteTime"//拼接分钟数
        }
        val surplusSec = musicTime % 60
        val surplus = if (surplusSec > 10) {
            Integer.toString(surplusSec)
        } else {
            "0$surplusSec"
        }
        return "$min:$surplus"
    }

}
