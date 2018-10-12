package com.lq.mxmusic.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import java.util.ArrayList
/*
*2018/9/30 0030  11:35
*ViewPager Fragment Adapter by liu
*/
class FragmentViewPagerAdapter constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var fragments = ArrayList<Fragment>()

    fun setData(mList: List<Fragment>?) {
        if (mList != null && mList.isNotEmpty()) {
            fragments.clear()
            fragments.addAll(mList)
            notifyDataSetChanged()
        }
    }
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}