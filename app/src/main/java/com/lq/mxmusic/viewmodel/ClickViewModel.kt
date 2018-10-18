package com.lq.mxmusic.viewmodel

import android.view.View

/*
*2018/10/12 0012  13:52
*点击事件 by lq
*/
class ClickViewModel {


    fun toMusicPlay(view: View) {
        ShowUtils.showInfo(view.context,"ClickViewModel")
    }
}