package com.lq.mxmusic.reposity.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lq.mxmusic.reposity.entity.LocalMusicEntity

/*
*2018/10/10 0010  15:55
*本地音乐操作类 by lq
*/
@Dao
interface LocalMusicDao{

    @Insert
    fun insertMusic(music:LocalMusicEntity)

    @Insert
    fun insertAll(list:List<LocalMusicEntity>)

    @Delete
    fun deleteMusic(music:LocalMusicEntity)


    @Query("select * from LocalMusicEntity")
    fun queryAll():List<LocalMusicEntity>

}