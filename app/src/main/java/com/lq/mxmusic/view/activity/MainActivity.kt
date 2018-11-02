package com.lq.mxmusic.view.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lq.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.view.adapter.FragmentViewPagerAdapter
import com.lq.mxmusic.view.fragment.FriendFragment
import com.lq.mxmusic.view.fragment.LocalFragment
import com.lq.mxmusic.view.fragment.MusicFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.lq.mxmusic.callback.LifeCallBack
import com.lq.mxmusic.util.StatusBarUtil
import com.lq.mxmusic.util.ThemeUtils
import kotlinx.android.synthetic.main.activity_music_play.*


/*
*2018/10/8 0008  16:24
*MainActivity by lq
*/
class MainActivity : BaseActivity() {
    private var clickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        forbidShowToolbar()
        adapterStatus()
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

    private fun adapterStatus() {
        val height = StatusBarUtil.getStatusBarHeight(this)
        val params = mainStatusView.layoutParams
        params.height = height
        mainStatusView.layoutParams = params
        //透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
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
        if (item?.itemId == android.R.id.home) {
            mainDrawerLayout.openDrawer(Gravity.START)

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initNavigation() {
        val header = mainNavigation.getHeaderView(0)
        val bgIv = header.findViewById<ImageView>(R.id.main_default_bgIv)
        val headerIv = header.findViewById<ImageView>(R.id.main_default_headerIv)
        Glide.with(this).load(R.drawable.default_header).asGif().into(headerIv)
        Glide.with(this).load(R.drawable.default_bg).into(bgIv)

        mainNavigation.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.menu_about_app -> {
                    startActivity(Intent(this, AboutAppActivity::class.java))
                    mainDrawerLayout.closeDrawer(Gravity.START)
                }
                R.id.menu_mode_switch -> {
                    showAnimation()
                    ThemeUtils.changeDayNightTheme(this)
                    recreate()
                }
                else -> Log.w("MainActivity", "why you could call this ")
            }
            true
        }
    }


    /*切换夜间模式的动画    看起来并没有什么效果？*/
    private fun showAnimation() {
        val decorView = window.decorView
        val cacheBitmap = getCacheBitmapFromView(decorView)
        if (decorView is ViewGroup && cacheBitmap != null) {
            val view = View(this)
            view.setBackgroundDrawable(BitmapDrawable(resources, cacheBitmap))
            val layoutParam = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            decorView.addView(view, layoutParam)
            val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            objectAnimator.duration = 300
            objectAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    decorView.removeView(view)
                }
            })
            objectAnimator.start()
        }
    }

    private fun getCacheBitmapFromView(view: View): Bitmap? {
        val drawingCacheEnabled = true
        view.isDrawingCacheEnabled = drawingCacheEnabled
        view.buildDrawingCache(drawingCacheEnabled)
        val drawingCache = view.drawingCache
        val bitmap: Bitmap?
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache)
            view.isDrawingCacheEnabled = false
        } else {
            bitmap = null
        }
        return bitmap
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - clickTime <= 2 * 1000) {
                LifeCallBack.finishAll()
                finish()
            } else {
                ShowUtils.showSuccess(this, "再点击一次返回键退出应用")
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