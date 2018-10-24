package com.lq.mxmusic.util

import android.app.Activity
import android.support.v4.content.ContextCompat
import com.lq.mxmusic.R
import com.lq.mxmusic.reposity.config.AppConfig

/*
*2018/10/19 0019  10:15
*function by lq
*/
object ThemeUtils {

    //判断当前theme
    fun setTheme(activity: Activity) {
        val currentTheme = SharedPreferencesUtil.getCurrentTheme()
        when (currentTheme) {
            AppConfig.THEME_DEFAULT -> {
                activity.setTheme(R.style.DayTheme)
                SharedPreferencesUtil.setCurrentTheme(AppConfig.THEME_DEFAULT)
                StatusBarUtil.setColor(activity, ContextCompat.getColor(activity,R.color.transparent), 0)
            }
            AppConfig.THEME_NIGHT -> {
                activity.setTheme(R.style.NightTheme)
                SharedPreferencesUtil.setCurrentTheme(AppConfig.THEME_NIGHT)
                StatusBarUtil.setColor(activity, ContextCompat.getColor(activity,R.color.night_bg_color), 0)

            }
        }
    }

    /*日间 夜间模式切换*/
    fun changeDayNightTheme(activity:Activity){
        val theme = SharedPreferencesUtil.getCurrentTheme()
        when(theme){
            AppConfig.THEME_DEFAULT ->{
                activity.setTheme(R.style.NightTheme)
                SharedPreferencesUtil.setCurrentTheme(AppConfig.THEME_NIGHT)
                StatusBarUtil.setColor(activity, ContextCompat.getColor(activity,R.color.night_bg_color), 0)
            }
            AppConfig.THEME_NIGHT -> {
                activity.setTheme(R.style.DayTheme)
                SharedPreferencesUtil.setCurrentTheme(AppConfig.THEME_DEFAULT)
                StatusBarUtil.setColor(activity, ContextCompat.getColor(activity,R.color.colorPrimary), 0)
            }
        }
    }
}