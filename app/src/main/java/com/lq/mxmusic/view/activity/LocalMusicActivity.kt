package com.lq.mxmusic.view.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Menu
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.view.adapter.FragmentViewPagerAdapter
import com.lq.mxmusic.view.fragment.SingleMusicFragment
import kotlinx.android.synthetic.main.activity_local_music.*

/*
*2018/10/10 0010  11:28
*本地音乐 by lq
*/
class LocalMusicActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_music)
        setTitleText(R.string.local_music)

        initData()
        initListener()
    }
    private fun initListener(){
        localViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                localTabLayout.getTabAt(p0)?.select()
            }

            override fun onPageSelected(p0: Int) {
            }
        })
        localTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                localViewPager.currentItem = p0!!.position
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
            }
        })
    }

    private fun initData(){
        val mList = ArrayList<Fragment>()
        val adapter = FragmentViewPagerAdapter(supportFragmentManager)
        mList.add(SingleMusicFragment())
        localViewPager.adapter = adapter
        adapter.setData(mList)
        localTabLayout.addTab(localTabLayout.newTab().setText("单曲"))
        localTabLayout.addTab(localTabLayout.newTab().setText("歌手"))
        localTabLayout.addTab(localTabLayout.newTab().setText("专辑"))
        localTabLayout.addTab(localTabLayout.newTab().setText("文件夹"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_with_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}