package com.lq.mxmusic.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.util.SafeClickListener
import com.lq.mxmusic.view.activity.LocalMusicActivity
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
        val localNumber = SharedPreferencesUtil.getLocalMusicNumber()//本地数量
        val nearlyNumber = SharedPreferencesUtil.getNearlyMusicPlayNumber()//最近播放数量
        val downloadNumber = SharedPreferencesUtil.getDownLoadNumber()//下载管理数量
        localNumTv.text = "$localNumber"
    }

    private fun initListener() {
        localLL.setOnClickListener(object : SafeClickListener() {
            override fun onNoDoubleClick(v: View) {
//                showBoardBottom()
                startActivity(Intent(v.context, LocalMusicActivity::class.java))
            }
        })
        localRefreshLayout.setOnRefreshListener {
            //todo 从数据库查询 各个表的数据
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