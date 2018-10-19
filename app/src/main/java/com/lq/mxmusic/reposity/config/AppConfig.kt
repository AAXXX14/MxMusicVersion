package com.lq.mxmusic.reposity.config

import com.lq.mxmusic.reposity.entity.LocalMusicEntity

/*
*2018/9/19 0019  11:33
*使用常量 by liu
*/
object AppConfig {
    const val BASE_URL = ""
    const val DEBUG = true
    const val SP_DEFAULT_NAME = "lq_mx_default_share_preference"
    const val LOCAL_MUSIC_NUMBER = "local_music_number" //本地音乐数量
    const val NEARLY_MUSIC_PLAY_NUMBER = "nearly_music_play_number"//最近播放 数量
    const val MUSIC_DOWNLOAD_MANAGER_NUMBER = "music_download_manager_number"//下载管理 数量
    const val MUSIC_CURRENT_PLAY_POSITION = "music_current_play_position"
    const val APP_DAY_NIGHT_MODE = "app_day_night_mode" //夜间模式
    const val ERROR = "error"


    /*播放*/
    const val PLAY_ENTITY = "entity"
    const val PLAY_SOURCE = "lq_play_source"
    const val PLAY_POSITION = "lq_play_position"
    const val PLAY_LOCAL = 1//本地
    const val PLAY_NEARLY = 2//最近
    var localPlayList: ArrayList<LocalMusicEntity>? = null

    const val NEEDLE_BACK_ANIMATOR_DURATION = 500.toLong()
    const val NEEDLE_GO_ANIMATION_DURATION = 200.toLong()
    const val ROTATE_ANIMATOR_DURATION = (1000 * 20).toLong()

    const val CURRENT_THEME = "lq_current_theme"
    const val THEME_DEFAULT = 1001 //默认主题
    const val THEME_NIGHT = 1002//夜间出题

    /*二维码扫描结果*/
    const val SCAN_REQUEST_CODE = 14
}