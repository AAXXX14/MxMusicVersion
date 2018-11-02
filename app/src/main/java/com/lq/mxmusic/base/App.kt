package com.lq.mxmusic.base

import android.app.Application
import com.lq.mxmusic.callback.LifeCallBack

/*
*2018/10/9 0009  9:47
*function by lq
*/
class App : Application(){

    override fun onCreate() {
        instance = this
        registerActivityLifecycleCallbacks(LifeCallBack)
        super.onCreate()
        SharedPreferencesUtil.setPlayPosition(0)
    }

    companion object {
        lateinit var instance:App
    }

}