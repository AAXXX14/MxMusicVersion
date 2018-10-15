package com.lq.mxmusic.view.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.entity.CurrentMusicEntity
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.view.adapter.PlayDiscViewPagerAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music_play.*


/*
*2018/10/12 0012  11:22
*播放 by lq
*/
class MusicPlayActivity : BaseActivity() {
    private val playList = ArrayList<String>()//当前播放的列表  的 图片
    private val musicAllList = ArrayList<CurrentMusicEntity>()//全部播放列表

    //将当前 列表 添加到 CurrentPlayList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)
        setFitSystem(false)
        setStatusColor(R.color.transparent)
        setToolbarColor(R.color.transparent)
        //设置副标题主标题
        showToolBar(false)
        initData()
    }

    private fun initData() {
        val playSource = intent.getIntExtra(AppConfig.PLAY_SOURCE, 1)//默认是本地的
        when (playSource) {
            AppConfig.PLAY_LOCAL -> {//本地播放
                val entity = intent.getParcelableExtra<LocalMusicEntity>(AppConfig.PLAY_ENTITY)
                //todo 标识当前播放列表为 本地播放
                val playName = entity.musicName
                val singerName = entity.musicSingerName
                playNameTv.text = playName
                singerNameTv.text = singerName
                //todo 通知播放
            }
        }

        playViewPager.adapter = PlayDiscViewPagerAdapter(playList)
        Glide.with(this).load(R.drawable.default_bg).bitmapTransform(BlurTransformation(this, 25, 8)).into(playBgIv)
    }
}