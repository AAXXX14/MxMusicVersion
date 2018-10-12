package com.lq.mxmusic.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseFragment
import com.lq.mxmusic.view.adapter.FragmentViewPagerAdapter
import com.lq.mxmusic.view.widget.StatusRecyclerView
import kotlinx.android.synthetic.main.fragment_local_music.*
import kotlinx.android.synthetic.main.fragment_music.*

/*
*2018/10/9 0009  13:01
*音乐 by lq
*/
class MusicFragment :BaseFragment(){
    override fun setContent(): Int {
       return  R.layout.fragment_music
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initListener()
    }

    private fun initListener() {
        musicViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                musicTabLayout.getTabAt(p0)?.select()
            }
        })
        musicTabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                musicViewPager.currentItem = p0!!.position
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })
    }

    private fun initData() {
        val mList = ArrayList<Fragment>()
        val adapter = FragmentViewPagerAdapter(childFragmentManager)
        mList.add(RecommendFragment())
        mList.add(LeaderBoardFragment())
        mList.add(RadioFragment())
        musicViewPager.adapter =adapter
        adapter.setData(mList)
        musicTabLayout.addTab(musicTabLayout.newTab().setText("推荐"))
        musicTabLayout.addTab(musicTabLayout.newTab().setText("榜单"))
        musicTabLayout.addTab(musicTabLayout.newTab().setText("电台"))
    }


}