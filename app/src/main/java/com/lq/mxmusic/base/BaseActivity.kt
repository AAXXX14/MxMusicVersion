package com.lq.mxmusic.base

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.util.DisplayUtils
import com.lq.mxmusic.util.SafeClickListener
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_base.*

/*
*2018/10/8 0008  15:14
*function ... by lq
*/
open class BaseActivity : AppCompatActivity() {
    private var hasBackIcon = true
    private lateinit var mContentView: ViewGroup
    private val emptyView by lazy { layoutInflater.inflate(R.layout.layout_empty_view, null) }
    private val errorView by lazy { layoutInflater.inflate(R.layout.layout_error_view, null) }
    private val loadFailView by lazy {layoutInflater.inflate(R.layout.layout_load_fail_view,null)}
    private val loadingView by lazy {layoutInflater.inflate(R.layout.layout_loading_view,null)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
        mContentView = findViewById(android.R.id.content)
        setStatusColor(R.color.colorPrimary)
        DisplayUtils.setCustomDensity(this, App.instance)
        setToolbar()
        initData()
        initListener()
    }

    private fun initData() {
//        showLoading()
    }

    private fun initListener() {
        baseActivityToolbar.setNavigationOnClickListener { onBackPressed() }
        multipleRl.setOnClickListener(object : SafeClickListener() {
              override fun onNoDoubleClick(v: View) {
                  showLoading()
                  onRefresh()
              }
          })
    }

    protected fun showEmpty() {
        multipleRl.visibility = View.VISIBLE
        multipleRl.removeAllViews()
        multipleRl.addView(emptyView)
    }

    protected fun showLoadFail() {
        multipleRl.visibility = View.VISIBLE
        multipleRl.removeAllViews()
        multipleRl.addView(loadFailView)
    }

    protected fun showError(){
        multipleLl.visibility = View.VISIBLE
        multipleLl.removeAllViews()
        multipleLl.addView(errorView)
    }

    protected fun showLoading() {
        multipleRl.visibility = View.VISIBLE
        multipleRl.removeAllViews()
        multipleRl.addView(loadingView)
    }

    protected fun showContent() {
        multipleRl.visibility = View.GONE
    }

    /**
     * 失败后点击刷新
     */
    protected fun onRefresh() {}

    /*默认显示加载动画*/


    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, container, false)
        container.id = android.R.id.content
        mContentView.id = View.NO_ID
        container.removeAllViews()
        container.addView(view)
    }

    protected fun setStatusColor(colorPrimary: Int) {
//        StatusBarUtil.setColor(this, colorPrimary, 0)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = ContextCompat.getColor(this, colorPrimary)
        }
    }

    protected fun setTitleText(title: Int) {
        mTitleTv.text = getString(title)
    }

    protected fun setRightText(right: Int) {
        mRightTv.text = getString(right)
    }

    protected fun showRightTv() {
        mRightTv.visibility = View.VISIBLE
    }

    protected fun hasBackIcon(icon: Boolean) {
        hasBackIcon = icon
    }

    protected fun showToolBar(show: Boolean) {
        if (show)
            baseActivityToolbar.visibility = View.VISIBLE
        else
            baseActivityToolbar.visibility = View.GONE
    }

    private fun setToolbar() {
        setSupportActionBar(baseActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackIcon)
    }
}