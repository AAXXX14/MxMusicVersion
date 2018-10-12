package com.lq.mxmusic.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.lq.administrator.mxmusic.R

/*
*2018/10/10 0010  14:34
*索引排序侧边栏  by lq
*/
class MusicSlideToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private val cylinderPaint by lazy { Paint() }//圆柱画笔
    private val textPaint by lazy { Paint() }//文字画笔
    private val textContent = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "*")
    private var mHeight: Int = 0  //控件整体高度
    private var mWidth: Int = 0 //控件整体宽度
    private var textHeight: Int = 0//一个文字的高度
    private var textView: TextView? = null
    private var chooseItem = -1
    private var listener: OnTouchingTextChangedListener? = null

    init {
        initData()
    }

    fun setTextView(textView: TextView) {
        this.textView = textView
    }

    private fun initData() {
        //初始化画笔
        cylinderPaint.color = Color.GRAY
        cylinderPaint.isAntiAlias = true
        cylinderPaint.strokeWidth = 8f

        textPaint.isAntiAlias = false
        textPaint.isFakeBoldText = true
        textPaint.strokeWidth = 8f
        textPaint.textSize = 14f
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = measuredHeight
        mWidth = measuredWidth
        textHeight = mHeight / textContent.size
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画圆柱

        //画文字
        for (i in 0 until textContent.size) {
            if (i == chooseItem)
                textPaint.color = Color.GREEN
            else
                textPaint.color = Color.BLUE

            val xPos = width / 2 - textPaint.measureText(textContent[i]) / 2
            val yPos = (textHeight * i + textHeight).toFloat()
            canvas?.drawText(textContent[i], xPos, yPos, textPaint)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val currentPosition = event!!.y
        val oldChooseItem = chooseItem
        val item = (currentPosition / mHeight * textContent.size).toInt()
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                chooseItem = -1
                setBackgroundColor(Color.TRANSPARENT)
                invalidate()
                textView?.visibility = View.GONE
            }
            else -> {
                setBackgroundColor(resources.getColor(R.color.gray))
                textView?.visibility = View.VISIBLE
                if (item >= 0 && item < textContent.size && item != oldChooseItem && listener != null) {
                    listener!!.onTouchingTextChanged(textContent[item])
                    textView?.text = textContent[item]
                    textView?.visibility = View.VISIBLE
                }
                chooseItem = item
                invalidate()
            }
        }
        return true
    }

    fun setTouchingTextChangeListener(listener: OnTouchingTextChangedListener) {
        this.listener = listener
    }

    interface OnTouchingTextChangedListener {
        fun onTouchingTextChanged(s: String)
    }

}