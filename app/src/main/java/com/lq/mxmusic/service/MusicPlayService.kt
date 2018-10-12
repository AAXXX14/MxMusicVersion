package com.lq.mxmusic.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/*
*2018/10/12 0012  11:27
*播放 by lq
*/
class MusicPlayService :Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}