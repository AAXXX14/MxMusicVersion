package com.lq.mxmusic.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.lq.administrator.mxmusic.R


/*
*2018/10/9 0009  11:09
*function by lq
*/
class MultiRecyclerView : LinearLayout {
    @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attributeSet, defStyleAttr){
        initData(context)
    }

    private fun initData(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_multi_recycler,this,true)

    }


}