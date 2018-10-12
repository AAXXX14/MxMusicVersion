package com.lq.mxmusic.view.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.entity.CurrentMusicEntity
import com.lq.mxmusic.view.adapter.PlayDiscViewPagerAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_music_play.*

/*
*2018/10/12 0012  11:22
*播放 by lq
*/
class MusicPlayActivity :BaseActivity(){
    private val playList = ArrayList<View>()//当前播放的列表
    private val musicAllList = ArrayList<CurrentMusicEntity>()//全部播放列表

    //将当前 列表 添加到 CurrentPlayList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)
        setToolbarColor(R.color.transparent)
        //设置副标题主标题
        baseActivityToolbar.title = "主标题"
        baseActivityToolbar.subtitle = "副标题"

        initData()
    }

    private fun initData(){
        playViewPager.adapter = PlayDiscViewPagerAdapter(playList)
        val option = RequestOptions.bitmapTransform(BlurTransformation(this,25,8))
        Glide.with(this).load(R.drawable.default_bg).apply(option).into(playBgIv)
    }
}