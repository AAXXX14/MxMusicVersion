package com.lq.mxmusic.reposity.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity

/*
*2018/10/12 0012  14:11
*最近播放 by lq
*/
@Dao
interface NearlyMusicDao {
    @Insert
    fun insertMusic(music: NearlyMusicEntity)

    @Insert
    fun insertAll(list: List<NearlyMusicEntity>)

    @Delete
    fun deleteMusic(music: NearlyMusicEntity)

    @Delete
    fun deleteAll(list:ArrayList<NearlyMusicEntity>) :Int

    @Query("select * from NearlyMusicEntity where musicPath= :path")
    fun queryByPath(path: String): NearlyMusicEntity?

    @Query("select * from NearlyMusicEntity")
    fun queryAll(): List<NearlyMusicEntity>

}