package com.lq.mxmusic.view.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.lq.administrator.mxmusic.R
import kotlinx.android.synthetic.main.state_recyclerview.view.*
import android.view.ViewGroup


/*
 *2018/9/25 19:30
 *多状态RecyclerView by liu
 */
class StatusRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RelativeLayout(context, attrs, defStyle) {
    private var isEnable = false
    private lateinit var errorView: ViewGroup
    private lateinit var emptyView: ViewGroup
    private lateinit var loadingView:ViewGroup
    private lateinit var loadFailView:ViewGroup

    init {

        initAttr(attrs)
        initData()
    }

    private fun initAttr(attrs: AttributeSet? = null) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StatusRecyclerView)
        val errorId = attributes.getResourceId(R.styleable.StatusRecyclerView_error_view, R.layout.layout_error_view)
        errorView = LayoutInflater.from(context).inflate(errorId, null) as ViewGroup
        val emptyId = attributes.getResourceId(R.styleable.StatusRecyclerView_empty_view,R.layout.layout_empty_view)
        emptyView = LayoutInflater.from(context).inflate(emptyId,null) as ViewGroup
        val loadingId = attributes.getResourceId(R.styleable.StatusRecyclerView_loading_view,R.layout.layout_loading_view)
        loadingView = LayoutInflater.from(context).inflate(loadingId,null) as ViewGroup
        val loadingFailId =attributes.getResourceId(R.styleable.StatusRecyclerView_load_fail_view,R.layout.layout_load_fail_view)
        loadFailView = LayoutInflater.from(context).inflate(loadingFailId,null) as ViewGroup
        attributes.recycle()
    }


    private fun initData() {
        LayoutInflater.from(context).inflate(R.layout.state_recyclerview, this, true)
    }


    fun changeState(state: Int) {
        when (state) {
            STATE_EMPTY -> {
                statusRl.visibility = View.VISIBLE
                statusRl.removeAllViews()
                statusRl.addView(emptyView)
            }
            STATE_ERROR -> {
                statusRl.visibility = View.VISIBLE
                statusRl.removeAllViews()
                statusRl.addView(errorView)
            }
            STATE_LOADING -> {
                statusRl.visibility = View.VISIBLE
                statusRl.removeAllViews()
                statusRl.addView(loadingView)
            }
            STATE_NORMAL -> statusRl.visibility = View.GONE
            else -> Log.wtf("Nothing to do","i just want to print an log,just so so")
        }
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        stateRv.layoutManager = layoutManager
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        stateRv.adapter = adapter
    }

    companion object {
        const val STATE_EMPTY = 1
        const val STATE_ERROR = 2
        const val STATE_LOADING = 3
        const val STATE_NORMAL = 4
    }

   /* private fun setErrorView(view: View) {}

    private fun setEmptyView(view: View) {}

    private fun setLoadingView(view: View) {}*/
}
