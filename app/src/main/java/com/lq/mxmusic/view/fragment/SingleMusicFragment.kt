package com.lq.mxmusic.view.fragment

import android.os.Bundle
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
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
    private val testList = ArrayList<String>()

    override fun setContent(): Int {
        return R.layout.fragment_single_music
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        slideToolBar.setTextView(sortLetterTv)
        slideToolBar.setTouchingTextChangeListener(object:MusicSlideToolBar.OnTouchingTextChangedListener{
            override fun onTouchingTextChanged(s: String) {

            }
        })
     /*   val all = AppDataBase.instance.localMusicDao().queryAll()
        mList.clear()
        mList.addAll(all)
        singleRv.adapter = adapter
        if (all.isEmpty()) {
            showEmpty()
        }*/
        testList.add("AAA")
        testList.add("AAA")
        testList.add("AAA")
        testList.add("BBB")
        testList.add("BBB")
        testList.add("BBB")
        testList.add("CCC")
    }

    override fun retry() {
        showLoading()
        //todo 搜索手机文件 并添加到数据库中去

        adapter.notifyDataSetChanged()
        showContent()
    }


}