package com.lq.mxmusic.reposity.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.lq.mxmusic.reposity.entity.CurrentMusicEntity

/*
*2018/10/11 0011  15:17
*function by lq
*/
@Dao
interface CurrentPlayDao{

    @Query("select * from currentMusicEntity")
    fun queryAll():List<CurrentMusicEntity>
}