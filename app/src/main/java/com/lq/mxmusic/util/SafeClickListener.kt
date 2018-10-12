package com.lq.mxmusic.util

import android.view.View
import android.view.View.OnClickListener

import java.util.Calendar

/**
 * 避免在1秒内出发多次点击
 */
abstract class SafeClickListener : OnClickListener {
    private var lastClickTime: Long = 0
    private var id = -1

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        val mId = v.id
        if (id != mId) {
            id = mId
            lastClickTime = currentTime
            onNoDoubleClick(v)
            return
        }
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    protected abstract fun onNoDoubleClick(v: View)

    companion object {
       const val MIN_CLICK_DELAY_TIME = 1000
    }
}
