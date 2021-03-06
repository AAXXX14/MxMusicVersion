package com.lq.mxmusic.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lq.mxmusic.R
import com.lq.mxmusic.callback.LifeCallBack
import com.lq.mxmusic.callback.SafeClickCallBack
import com.lq.mxmusic.reposity.config.PlayConfig
import com.lq.mxmusic.reposity.event.MusicPlayServiceEvent
import com.lq.mxmusic.util.*
import com.lq.mxmusic.view.activity.MusicPlayActivity
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/*
*2018/10/8 0008  15:14
*function ... by lq
*/
open class BaseActivity : AppCompatActivity() {
    private var hasBackIcon = true
    private lateinit var mContentView: ViewGroup
    private lateinit var provider: ViewModelProvider
    private val emptyView by lazy { layoutInflater.inflate(R.layout.layout_empty_view, null) }
    private val errorView by lazy { layoutInflater.inflate(R.layout.layout_error_view, null) }
    private val loadFailView by lazy { layoutInflater.inflate(R.layout.layout_load_fail_view, null) }
    private val loadingView by lazy { layoutInflater.inflate(R.layout.layout_loading_view, null) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setTheme(this)
        super.setContentView(R.layout.activity_base)
        mContentView = findViewById(android.R.id.content)
        provider = ViewModelProviders.of(this)
        DisplayUtils.setCustomDensity(this, App.instance)
        setToolbar(baseActivityToolbar)
        initData()
        initListener()
        EventBus.getDefault().register(this)
        //todo 默认开启服务
        ServiceUtil.startPlayService(this)
    }

    private fun initData() {
//        showLoading()
        if (ContainerBottomUtil.isEmptyData()){
            forbidShowBottom()
        }else{
            showBottomTop()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun bottomStateChangeEvent(event: MusicPlayServiceEvent.MusicPlayChangeStateEvent) {
        if (PlayConfig.IS_PLAY) {
            showBottomTop()
            if(javaClass.simpleName == MusicPlayActivity::class.java.simpleName){
                forbidShowBottom()

            }
        } else {
            forbidShowBottom()
        }
    }

    private fun initListener() {
        baseActivityToolbar.setNavigationOnClickListener { onBackPressed() }
        multipleRl.setOnClickListener(object : SafeClickCallBack() {
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

    protected fun showError() {
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

    protected fun forbidShowBottom() {
        containerBottomLL.visibility = View.GONE
        val parameters = container.layoutParams as ViewGroup.MarginLayoutParams
        parameters.bottomMargin = 0
    }

    protected fun showBottomTop() {
        containerBottomLL.visibility = View.VISIBLE
        val parameters = container.layoutParams as ViewGroup.MarginLayoutParams
        parameters.bottomMargin = 150
    }

    /**
     * 失败后点击刷新
     */
    protected fun onRefresh() {}

    protected fun setFitSystem(fit: Boolean) {
//        ll_root.fitsSystemWindows = fit
    }

    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, container, false)
        container.id = android.R.id.content
        mContentView.id = View.NO_ID
        container.removeAllViews()
        container.addView(view)
    }

    protected fun setStatusColor(colorPrimary: Int) {
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, colorPrimary), 255)
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

    protected fun forbidShowToolbar() {
        baseActivityToolbar.visibility = View.GONE
    }

    protected fun setToolbarColor(color: Int) {
        baseActivityToolbar.background = ContextCompat.getDrawable(this, color)
    }

    protected fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackIcon)
    }

    fun <T : ViewModel> getViewModel(modelClass: Class<T>): T = provider.get(modelClass)

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}