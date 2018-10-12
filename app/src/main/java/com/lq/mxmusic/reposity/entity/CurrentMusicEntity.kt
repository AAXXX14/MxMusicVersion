package com.lq.mxmusic.reposity.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/*
*2018/10/10 0010  17:23
* 当前播放 数据表 by lq
*/
@Entity
data class CurrentMusicEntity(
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        val musicName:String,//歌曲名称
        val musicSingerName:String,//歌手名
        val musicPath:String,//播放位置
        val musicProgress:Int//播放进度
)