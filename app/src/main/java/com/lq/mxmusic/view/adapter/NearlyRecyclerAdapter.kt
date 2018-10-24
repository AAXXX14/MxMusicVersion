package com.lq.mxmusic.view.adapter

import android.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.mxmusic.R
import com.lq.mxmusic.databinding.NearlyRecyclerItemBinding
import com.lq.mxmusic.reposity.entity.NearlyMusicEntity

/*
*2018/10/18 0018  13:49
*最近播放 adapter by lq
*/
class NearlyRecyclerAdapter(mList:List<NearlyMusicEntity>) :BaseQuickAdapter<NearlyMusicEntity,BaseViewHolder>(R.layout.nearly_recycler_item,mList){
    override fun convert(helper: BaseViewHolder?, item: NearlyMusicEntity?) {
        val bind = DataBindingUtil.bind<NearlyRecyclerItemBinding>(helper!!.itemView)
        bind!!.entity = item
        bind.executePendingBindings()
        helper.addOnClickListener(R.id.musicItemRl)
    }

}