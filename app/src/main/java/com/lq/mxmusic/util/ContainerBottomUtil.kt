package com.lq.mxmusic.util

import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.database.AppDataBase

/*
*2018/11/1 0001  11:44
*function by lq
*/
object ContainerBottomUtil {

    /*当前存储的是否为空数据*/
    fun isEmptyData(): Boolean {
        val sourceList = AppConfig.localPlayList
        if (sourceList != null) {
            return false
        } else {
            val source = SharedPreferencesUtil.getPlaySource()
            when (source) {
                AppConfig.PLAY_LOCAL -> {//查询该数据库中数据添加进去
                    val list = AppDataBase.instance.localMusicDao().queryAll()
                    return list.isEmpty()
                }
            }
        }
        return true
    }
}