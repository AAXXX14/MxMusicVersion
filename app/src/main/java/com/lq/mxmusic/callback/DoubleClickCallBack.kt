package com.lq.mxmusic.callback

import android.view.View
import java.util.*

/*
*2018/10/18 0018  14:58
*连续双击 by lq
*/
abstract class DoubleClickCallBack : View.OnClickListener{
    private var lastClickTime: Long = 0
    private var id = -1

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        val mId = v.id
        if (id != mId) {
            id = mId
            lastClickTime = currentTime
        }
        if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME) {
            //连续双击事件
            lastClickTime = currentTime
            onDirectDoubleClick(v)
        }
    }

    protected abstract fun onDirectDoubleClick(v: View)
    companion object {
        const val MIN_CLICK_DELAY_TIME = 1500
    }


}
