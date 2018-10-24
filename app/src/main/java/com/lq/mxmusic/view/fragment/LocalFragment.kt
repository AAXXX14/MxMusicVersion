package com.lq.mxmusic.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.lq.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.reposity.database.AppDataBase
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity
import com.lq.mxmusic.util.LogUtil
import com.lq.mxmusic.callback.SafeClickCallBack
import com.lq.mxmusic.view.activity.LocalMusicActivity
import com.lq.mxmusic.view.activity.NearlyPlayActivity
import kotlinx.android.synthetic.main.fragment_local_music.*

/*
*2018/10/9 0009  9:35
*个人 by lq
*/
class LocalFragment : BaseFragment() {
    override fun setContent(): Int {
        return R.layout.fragment_local_music
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initData()
    }

    private fun initData() {
        obtainLocalData()
    }

    private fun obtainLocalData() {
        val localNumber = AppDataBase.instance.localMusicDao().queryAll().size//本地数量
        val nearlyNumber = AppDataBase.instance.nearlyMusicDao().queryAll().size//最近播放数量
        val currentNumber = AppDataBase.instance.currentPlayDao().queryAll().size//当前播放 歌单数量
        val downloadNumber = SharedPreferencesUtil.getDownLoadNumber()//下载管理数量
        localNumTv.text = "($localNumber)"
        nearlyNumTv.text = "($nearlyNumber)"
        SharedPreferencesUtil.setLocalMusicNumber(localNumber)
        SharedPreferencesUtil.setNearlyMusicPlayNumber(nearlyNumber)
    }


    override fun onResume() {
        super.onResume()
        obtainLocalData()
    }

    private fun initListener() {
        localLL.setOnClickListener(object : SafeClickCallBack() {
            override fun onNoDoubleClick(v: View) {
//                showBoardBottom()
                startActivity(Intent(v.context, LocalMusicActivity::class.java))
            }
        })
        nearlyLL.setOnClickListener(object: SafeClickCallBack(){
            override fun onNoDoubleClick(v: View) {
                startActivity(Intent(v.context,NearlyPlayActivity::class.java))
            }
        })
        localRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        localRefreshLayout.setOnRefreshListener {
            // 查询各个表的数据
            val localNumber = AppDataBase.instance.localMusicDao().queryAll().size//本地数量
            val nearlyNumber = AppDataBase.instance.nearlyMusicDao().queryAll().size//最近播放数量
            val currentNumber = AppDataBase.instance.currentPlayDao().queryAll().size//当前播放 歌单数量
            val downloadNumber = SharedPreferencesUtil.getDownLoadNumber()//下载管理数量
            localNumTv.text = "($localNumber)"
            nearlyNumTv.text = "($nearlyNumber)"
            SharedPreferencesUtil.setLocalMusicNumber(localNumber)
            SharedPreferencesUtil.setNearlyMusicPlayNumber(nearlyNumber)
            localRefreshLayout.isRefreshing = false
        }
    }

    private fun showBoardBottom() {
        val mDialog = BottomSheetDialog(context!!, R.style.BottomSheetDialogStyle)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, null)
        val createBoardLL = view.findViewById(R.id.createBoardLL) as LinearLayout
        createBoardLL.setOnClickListener {
            ShowUtils.showInfo(createBoardLL.context, "创建中")
        }
        mDialog.setContentView(view)
        mDialog.setCancelable(true)
        mDialog.setCanceledOnTouchOutside(true)
        val parent = view.parent as ViewGroup
        parent.setBackgroundResource(android.R.color.transparent)
        mDialog.show()
    }

}