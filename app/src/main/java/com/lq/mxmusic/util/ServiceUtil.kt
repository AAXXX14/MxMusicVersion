package com.lq.mxmusic.util

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.lq.mxmusic.base.App


/*
*2018/10/17 0017  9:23
*function by lq
*/
object ServiceUtil {
    fun isRunning(name: String): Boolean {
        val manager = App.instance.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (name == service.service.className) {
                return true
            }
        }
        return false
    }
}