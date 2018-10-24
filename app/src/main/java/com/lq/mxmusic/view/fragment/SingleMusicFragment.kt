package com.lq.mxmusic.view.fragment

import android.os.Bundle
import com.lq.mxmusic.R
import com.lq.mxmusic.base.App
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.reposity.config.AppConfig
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.LocalMusicEntity
import com.lq.mxmusic.util.PlayUtils
import com.lq.mxmusic.util.ScanMusicUtils
import com.lq.mxmusic.view.adapter.MusicRecyclerAdapter
import com.lq.mxmusic.view.widget.MusicSlideToolBar
import com.tbruyelle.rxpermissions2.RxPermissions
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
            PlayUtils.preparePlay(view.context,position,AppConfig.PLAY_LOCAL,mList)
        }
    }



    //空数据 搜索
    override fun retry() {
        showLoading()
        val disposable = RxPermissions(this).request(android.Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
            if(it){
                val list = ScanMusicUtils.query(App.instance)
                AppDataBase.instance.localMusicDao().insertAll(list)
                SharedPreferencesUtil.setLocalMusicNumber(list.size)
                mList.addAll(list)
                adapter.notifyDataSetChanged()
            }
            else ShowUtils.showInfo(App.instance,"未开启读取权限,请在设置中打开")
        }
        disposable.dispose()
        showContent()
    }


}