package com.lq.mxmusic.view.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import com.lq.administrator.mxmusic.R


/**
 * Created by Administrator on 2017/9/13.
 */

class RecordNeedleImageView : ImageView {

    internal var flag = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (flag) {
            flag = false
            layout(left, resources.getDimension(R.dimen.top).toInt(), right, bottom)
        }
        flag = true
    }
}
