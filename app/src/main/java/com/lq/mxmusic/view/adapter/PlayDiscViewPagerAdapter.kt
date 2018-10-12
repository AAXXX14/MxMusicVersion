package com.lq.mxmusic.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/*
*2018/10/12 0012  13:22
*播放 viewPager by lq
*/

class PlayDiscViewPagerAdapter(private val mDiscLayouts: List<View>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val discLayout = mDiscLayouts[position]
        container.addView(discLayout)
        return discLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mDiscLayouts[position])
    }

    override fun getCount(): Int {
        return mDiscLayouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
