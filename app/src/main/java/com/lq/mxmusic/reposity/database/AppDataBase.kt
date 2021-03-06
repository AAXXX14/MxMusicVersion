package com.lq.mxmusic.reposity.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import com.lq.mxmusic.base.App
import com.lq.mxmusic.reposity.dao.CurrentPlayDao
import com.lq.mxmusic.reposity.dao.LocalMusicDao
import com.lq.mxmusic.reposity.dao.NearlyMusicDao
import com.lq.mxmusic.reposity.entity.CurrentMusicEntity
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity

/*
*2018/10/10 0010  11:27
*数据库 by lq
*/
@Database(entities = [LocalMusicEntity::class,CurrentMusicEntity::class,NearlyMusicEntity::class],version = 1,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun localMusicDao(): LocalMusicDao
    abstract fun currentPlayDao():CurrentPlayDao
    abstract fun nearlyMusicDao():NearlyMusicDao

    companion object {
        private const val DATABASE_NAME = "musicInfo.db"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(App.instance, AppDataBase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build() }
    }
}
