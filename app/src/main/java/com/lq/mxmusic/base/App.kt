package com.lq.mxmusic.base

import android.app.Application

/*
*2018/10/9 0009  9:47
*function by lq
*/
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance:App
    }

}