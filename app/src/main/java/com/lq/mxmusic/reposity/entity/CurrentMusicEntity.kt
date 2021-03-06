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
        var id: Int,
        var musicName: String,//歌曲名称
        var musicSingerName: String,//歌手名
        var musicPath: String,//播放位置
        var musicProgress: Int,//播放进度
        var musicLength: Long//歌曲总时长
)