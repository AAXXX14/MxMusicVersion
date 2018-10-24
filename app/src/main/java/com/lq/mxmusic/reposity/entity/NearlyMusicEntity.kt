package com.lq.mxmusic.reposity.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

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
        var musicLength: Long,//歌曲总时长
        var musicCoverUrl:String
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readLong(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(musicName)
                parcel.writeString(musicSingerName)
                parcel.writeString(musicPath)
                parcel.writeInt(musicProgress)
                parcel.writeLong(musicLength)
                parcel.writeString(musicCoverUrl)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<NearlyMusicEntity> {
                override fun createFromParcel(parcel: Parcel): NearlyMusicEntity {
                        return NearlyMusicEntity(parcel)
                }

                override fun newArray(size: Int): Array<NearlyMusicEntity?> {
                        return arrayOfNulls(size)
                }
        }
}