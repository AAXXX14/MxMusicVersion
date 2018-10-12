package com.lq.mxmusic.view.activity

import android.os.Bundle
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity

/*
*2018/10/8 0008  18:10
*关于应用 by lq
*/
class AboutAppActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        setTitleText(R.string.about_app)
    }


}