package com.lq.mxmusic.reposity.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/*
*2018/10/10 0010  15:47
*本地音乐数据 by lq
*/
@Entity
data class LocalMusicEntity(
       @PrimaryKey(autoGenerate = true)
        val id:Int,
        val musicName:String,//歌曲名称
        val musicSingerName:String,//歌手名
        val musicPath:String,//播放位置
        val musicProgress:Int//播放进度

)