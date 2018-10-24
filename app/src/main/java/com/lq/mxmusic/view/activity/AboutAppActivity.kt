package com.lq.mxmusic.view.activity

import android.os.Bundle
import com.lq.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.config.AppConfig
import kotlinx.android.synthetic.main.activity_about_app.*

/*
*2018/10/8 0008  18:10
*关于应用 by lq
*/
class AboutAppActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        setTitleText(R.string.about_app)

        mTv.setOnClickListener{
            SharedPreferencesUtil.setCurrentTheme(AppConfig.THEME_NIGHT)
            recreate()
        }
    }


}