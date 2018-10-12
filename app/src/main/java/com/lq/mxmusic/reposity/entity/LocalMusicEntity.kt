package com.lq.mxmusic.reposity.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable


/*
*2018/10/10 0010  15:47
*本地音乐数据 by lq
*/
@Entity
data class LocalMusicEntity(

        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var musicName: String,//歌曲名称
        var musicSingerName: String,//歌手名
        var musicPath: String,//播放位置
        var musicProgress: Int,//播放进度
        var musicLength: Long//歌曲总时长

) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readLong()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(musicName)
                parcel.writeString(musicSingerName)
                parcel.writeString(musicPath)
                parcel.writeInt(musicProgress)
                parcel.writeLong(musicLength)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<LocalMusicEntity> {
                override fun createFromParcel(parcel: Parcel): LocalMusicEntity {
                        return LocalMusicEntity(parcel)
                }

                override fun newArray(size: Int): Array<LocalMusicEntity?> {
                        return arrayOfNulls(size)
                }
        }
}