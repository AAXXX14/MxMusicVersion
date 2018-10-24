package com.lq.mxmusic.view.adapter

import android.animation.ObjectAnimator
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lq.mxmusic.R
import com.lq.mxmusic.reposity.config.AppConfig.ROTATE_ANIMATOR_DURATION

/*
*2018/10/12 0012  13:22
*播放 viewPager by lq
*/

class PlayDiscViewPagerAdapter(private val mDiscLayouts: List<String>) : PagerAdapter() {
    val mAnimationList = ArrayList<ObjectAnimator>()
    private lateinit var mDiscAnimator: ObjectAnimator

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val discLayout = LayoutInflater.from(container.context).inflate(R.layout.layout_disc, null)
        val iv = discLayout.findViewById<ImageView>(R.id.ivDisc)
        Glide.with(container.context).load(mDiscLayouts[position]).error(R.drawable.default_bg).into(iv)
        val animator = getAnimation(iv)
        mAnimationList.add(animator)
        container.addView(discLayout)
        return discLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mDiscLayouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    //设置动画旋转
    private fun getAnimation(mImageView: View): ObjectAnimator {
        mDiscAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f)
        mDiscAnimator.duration = ROTATE_ANIMATOR_DURATION
        mDiscAnimator.interpolator = LinearInterpolator()
        mDiscAnimator.repeatCount = Animation.INFINITE
        return mDiscAnimator
    }
}
