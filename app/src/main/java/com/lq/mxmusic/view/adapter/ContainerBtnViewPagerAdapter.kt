package com.lq.mxmusic.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.reposity.entity.LocalMusicEntity

import java.util.ArrayList

/**
 * Created by mx on 2017/10/20.
 */

class ContainerBtnViewPagerAdapter : PagerAdapter() {
    private val mList = ArrayList<LocalMusicEntity>()

    fun setData(list: List<LocalMusicEntity>?) {
        if ((list != null) and (list!!.isNotEmpty())) {
            mList.clear()
            mList.addAll(list)
            notifyDataSetChanged()
        }
    }


    override fun getCount(): Int {
        return mList.size
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.btn_container_item, null)
        val playbar_img = view.findViewById<ImageView>(R.id.playbar_img)
        val playbar_infoTv = view.findViewById<TextView>(R.id.playbar_info)
        val playbar_singer = view.findViewById<TextView>(R.id.playbar_singer)
        val musicDataBean = mList[position]
        playbar_singer.text = musicDataBean.musicSingerName
        playbar_infoTv.text = musicDataBean.musicName
        Glide.with(container.context).load(R.drawable.default_bg).error(R.drawable.placeholder_disk_210).into(playbar_img)
        container.addView(view)
        return view
    }
}
