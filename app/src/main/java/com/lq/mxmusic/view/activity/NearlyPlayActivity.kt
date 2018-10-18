package com.lq.mxmusic.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity
import com.lq.mxmusic.view.adapter.NearlyRecyclerAdapter
import kotlinx.android.synthetic.main.activity_nearly.*

/*
*2018/10/18 0018  13:42
*最近播放 by lq
*/
class NearlyPlayActivity:BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearly)
        setTitleText(R.string.nearly_play)
        initData()
    }

    private fun initData(){
        // 查询最近播放数据库
        val mList = ArrayList<NearlyMusicEntity>()
        mList.addAll(AppDataBase.instance.nearlyMusicDao().queryAll())
        val adapter = NearlyRecyclerAdapter(mList)
        nearlyRv.setLayoutManager(LinearLayoutManager(this))
        nearlyRv.setAdapter(adapter)
    }
}