package com.lq.mxmusic.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/*
*2018/10/9   9:48
*activityLifecycleCallBack by lq
*/
object LifeCallBack : Application.ActivityLifecycleCallbacks {
    private val activities = LinkedList<Activity>()
    fun add(activity: Activity) = activities.add(activity)
    private fun remove(activity: Activity) = activities.remove(activity)
    fun finishAll() {
        activities.filter { activity ->
            !activity.isFinishing
        }.forEach { ac ->
            ac.finish()
        }
    }

    fun exit() {
        finishAll()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        add(activity)
    }
}