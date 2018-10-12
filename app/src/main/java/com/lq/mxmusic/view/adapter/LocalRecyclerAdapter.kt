package com.lq.mxmusic.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.administrator.mxmusic.R

/*
*2018/10/9 0009  18:41
*歌单 RecyclerAdapter by lq
*/
class LocalRecyclerAdapter(mList:ArrayList<String>) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.local_recycler_item,mList){
    override fun convert(helper: BaseViewHolder?, item: String?) {
        
    }
}