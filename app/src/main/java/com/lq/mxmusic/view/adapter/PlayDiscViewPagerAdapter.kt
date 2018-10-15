package com.lq.mxmusic.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lq.administrator.mxmusic.R

/*
*2018/10/12 0012  13:22
*播放 viewPager by lq
*/

class PlayDiscViewPagerAdapter(private val mDiscLayouts: List<String>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val discLayout = LayoutInflater.from(container.context).inflate(R.layout.layout_disc, null)
        val iv = discLayout.findViewById<ImageView>(R.id.ivDisc)
        Glide.with(container.context).load(String).into(iv)
        container.addView(discLayout)
        return discLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mDiscLayouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
