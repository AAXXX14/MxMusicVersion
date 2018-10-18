package com.lq.mxmusic.reposity.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/*
*2018/10/12 0012  14:06
*最近播放 by lq
*/

@Entity
data class NearlyMusicEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var musicName: String,//歌曲名称
        var musicSingerName: String,//歌手名
        var musicPath: String,//播放位置
        var musicProgress: Int,//播放进度
        var musicLength: Long//歌曲总时长
)