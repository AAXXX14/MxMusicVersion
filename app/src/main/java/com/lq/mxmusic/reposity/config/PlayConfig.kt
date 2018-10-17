package com.lq.mxmusic.reposity.config

/*
*2018/10/16 0016  13:56
*function by lq
*/
object PlayConfig {
    var CURRENT_STATE = 0
    var IS_PLAY = false//是否在播放状态
    const val PLAY = 1//直接播放
    const val PAUSE = 2//暂停
    const val RESUME = 3//恢复
    const val NEXT = 4//下一首
    const val PREVIOUS = 5//上一首

}