package com.lq.mxmusic.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.App
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.callback.DoubleClickCallBack
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.util.PlayUtils
import com.lq.mxmusic.util.ScanMusicUtils
import com.lq.mxmusic.view.activity.MusicPlayActivity
import com.lq.mxmusic.view.adapter.MusicRecyclerAdapter
import com.lq.mxmusic.view.widget.MusicSlideToolBar
import kotlinx.android.synthetic.main.fragment_single_music.*

/*
*2018/10/10 0010  13:03
*单曲 by lq
*/
class SingleMusicFragment : BaseFragment() {
    private val mList = ArrayList<LocalMusicEntity>()
    private val adapter = MusicRecyclerAdapter(mList)

    override fun setContent(): Int {
        return R.layout.fragment_single_music
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        slideToolBar.setTextView(sortLetterTv)
        slideToolBar.setTouchingTextChangeListener(object : MusicSlideToolBar.OnTouchingTextChangedListener {
            override fun onTouchingTextChanged(s: String) {

            }
        })
        val all = AppDataBase.instance.localMusicDao().queryAll()
        SharedPreferencesUtil.setLocalMusicNumber(all.size)
        mList.clear()
        mList.addAll(all)
        singleRv.adapter = adapter
        if (all.isEmpty()) showEmpty()
        adapter.setOnItemChildClickListener { _, view, position ->
            PlayUtils.preparePlay(view.context, position, AppConfig.PLAY_LOCAL, mList)
            view.setOnClickListener(object : DoubleClickCallBack() {
                override fun onDirectDoubleClick(v: View) {
                    //连续双击
                    v.context.startActivity(Intent(v.context, MusicPlayActivity::class.java)
                            .putExtra(AppConfig.PLAY_ENTITY, mList[position]).putExtra(AppConfig.PLAY_SOURCE, AppConfig.PLAY_LOCAL))
                }

                override fun onOnceClick(v: View) {
                }
            })
        }
    }

    //空数据 搜索
    override fun retry() {
        showLoading()
        val list = ScanMusicUtils.query(App.instance)
        AppDataBase.instance.localMusicDao().insertAll(list)
        SharedPreferencesUtil.setLocalMusicNumber(list.size)
        mList.addAll(list)
        adapter.notifyDataSetChanged()
        showContent()
    }


}