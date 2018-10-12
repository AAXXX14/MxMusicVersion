package com.lq.mxmusic.reposity.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity

/*
*2018/10/12 0012  14:11
*function by lq
*/
@Dao
interface NearlyMusicDao{
    @Insert
    fun insertMusic(music: NearlyMusicEntity)

    @Insert
    fun insertAll(list:List<NearlyMusicEntity>)

    @Delete
    fun deleteMusic(music: NearlyMusicEntity)


    @Query("select * from LocalMusicEntity")
    fun queryAll():List<NearlyMusicEntity>

}