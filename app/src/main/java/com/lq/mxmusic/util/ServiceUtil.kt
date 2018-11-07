package com.lq.mxmusic.util

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import com.lq.mxmusic.base.App
import com.lq.mxmusic.service.MusicPlayService


/*
*2018/10/17 0017  9:23
*function by lq
*/
object ServiceUtil {
   private fun isRunning(name: String): Boolean {
        val manager = App.instance.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun startPlayService(context:Context){
        if(!isRunning(MusicPlayService::class.java.simpleName)) context.startService(Intent(context,MusicPlayService::class.java))
    }
}