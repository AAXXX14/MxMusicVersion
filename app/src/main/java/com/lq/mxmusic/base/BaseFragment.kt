package com.lq.mxmusic.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.util.DataBindingComponentUtil
import com.lq.mxmusic.callback.SafeClickCallBack
import kotlinx.android.synthetic.main.fragment_base.*

/*
*2018/10/9 0009  9:33
*BaseFragment by lq
*/
abstract class BaseFragment : Fragment() {
    private lateinit var bindingView: ViewDataBinding
    private var mIsVisible = false
    private val emptyView by lazy { layoutInflater.inflate(R.layout.layout_empty_view, null) }
    private val errorView by lazy { layoutInflater.inflate(R.layout.layout_error_view, null) }
    private val loadFailView by lazy { layoutInflater.inflate(R.layout.layout_load_fail_view, null) }
    private val loadingView by lazy { layoutInflater.inflate(R.layout.layout_loading_view, null) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ll = inflater.inflate(R.layout.fragment_base, null)
        bindingView = DataBindingUtil.inflate<ViewDataBinding>(inflater, setContent(), null, false, DataBindingComponentUtil)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        bindingView.root.layoutParams = params
        val mContainer = ll.findViewById<RelativeLayout>(R.id.fragmentContainer)
        mContainer.addView(bindingView.root)
        return ll
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        multipleLl.setOnClickListener(object : SafeClickCallBack() {
            override fun onNoDoubleClick(v: View) {
                showLoading()
                retry()
            }
        })
    }

    protected fun showEmpty() {
        multipleLl.visibility = View.VISIBLE
        multipleLl.removeAllViews()
        multipleLl.addView(emptyView)
    }

    protected fun showLoadFail() {
        multipleLl.visibility = View.VISIBLE
        multipleLl.removeAllViews()
        multipleLl.addView(loadFailView)
    }

    protected fun showError() {
        multipleLl.visibility = View.VISIBLE
        multipleLl.removeAllViews()
        multipleLl.addView(errorView)
    }

    protected fun showLoading() {
        multipleLl.visibility = View.VISIBLE
        multipleLl.removeAllViews()
        multipleLl.addView(loadingView)
    }

    protected fun showContent() {
        multipleLl.visibility = View.GONE
    }

    private fun onInvisible() {}

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected open fun loadData() {}

    protected open fun retry(){}

    private fun onVisible() = loadData()


    abstract fun setContent(): Int

}