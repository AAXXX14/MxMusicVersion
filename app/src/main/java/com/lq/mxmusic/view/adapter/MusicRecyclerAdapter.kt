package com.lq.mxmusic.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.reposity.entity.LocalMusicEntity

/*
*2018/10/10 0010  14:44
*function by lq
*/
class MusicRecyclerAdapter(mList:ArrayList<LocalMusicEntity>) :BaseQuickAdapter<LocalMusicEntity,BaseViewHolder>(R.layout.music_recycler_item,mList){
    override fun convert(helper: BaseViewHolder?, item: LocalMusicEntity?) {
    }

}