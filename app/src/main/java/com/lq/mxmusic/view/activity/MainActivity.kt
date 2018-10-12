package com.lq.mxmusic.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.view.adapter.FragmentViewPagerAdapter
import com.lq.mxmusic.view.fragment.FriendFragment
import com.lq.mxmusic.view.fragment.LocalFragment
import com.lq.mxmusic.view.fragment.MusicFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AppCompatDelegate
import android.view.KeyEvent
import com.lq.mxmusic.callback.LifeCallBack


/*
*2018/10/8 0008  16:24
*MainActivity by lq
*/
class MainActivity : BaseActivity() {
    private var clickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showToolBar(false)
        initToolbar()
        initNavigation()
        initViewPager()
        initListener()
    }

    private fun initListener() {
        localIv.setOnClickListener {
            mainViewPager.currentItem = 0
        }
        musicIv.setOnClickListener {
            mainViewPager.currentItem = 1
        }
        friendIv.setOnClickListener {
            mainViewPager.currentItem = 2
        }
    }

    private fun initViewPager() {
        val mList = ArrayList<Fragment>()
        val adapter = FragmentViewPagerAdapter(supportFragmentManager)
        mList.add(LocalFragment())//local room
        mList.add(MusicFragment())//music
        mList.add(FriendFragment())//friend
        mainViewPager.adapter = adapter
        adapter.setData(mList)
    }

    private fun initToolbar() {
        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mainToolbar.setOnMenuItemClickListener {
            true
        }
        mainToolbar.setNavigationIcon(R.drawable.titlebar_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            mainDrawerLayout.openDrawer(Gravity.START)
        return super.onOptionsItemSelected(item)
    }

    private fun initNavigation() {
        val header = mainNavigation.getHeaderView(0)
        val bgIv = header.findViewById<ImageView>(R.id.main_default_bgIv)
        val headerIv = header.findViewById<ImageView>(R.id.main_default_headerIv)
        Glide.with(this).asGif().load(R.drawable.default_header).into(headerIv)
        Glide.with(this).load(R.drawable.default_bg).into(bgIv)

        mainNavigation.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.menu_about_app -> {
                    startActivity(Intent(this, AboutAppActivity::class.java))
                    mainDrawerLayout.closeDrawer(Gravity.START)
                }
                R.id.menu_mode_switch -> {
                    val isNight = SharedPreferencesUtil.getDayNightMode()
                    if (isNight){
                        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        SharedPreferencesUtil.setDayNightMode(false)
                    }
                    else{
                        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        SharedPreferencesUtil.setDayNightMode(true)
                    }
                    recreate()
                }
                else -> Log.w("MainActivity", "why you could call this ")

            }
            true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - clickTime <= 2 * 1000) {
                LifeCallBack.finishAll()
                LifeCallBack.exit()
            } else {
                ShowUtils.showSuccess(this,"再点击一次返回键退出应用")
                clickTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }
}