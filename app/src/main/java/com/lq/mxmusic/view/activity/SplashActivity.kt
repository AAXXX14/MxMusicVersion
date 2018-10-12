package com.lq.mxmusic.view.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.config.CacheImageConfig
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import java.util.concurrent.TimeUnit

/*
*2018/10/8 0008  15:14
*闪屏页 _ 启动页 by liu
*/
class SplashActivity : BaseActivity() {
    private lateinit var  disposable :Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        showToolBar(false)
        val i = Random().nextInt(CacheImageConfig.SPLASH_URLS.size)
        val requestOption = RequestOptions().placeholder(R.mipmap.splash_bg).error(R.mipmap.splash_bg).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(this).applyDefaultRequestOptions(requestOption).load(CacheImageConfig.SPLASH_URLS[i]).transition(DrawableTransitionOptions().crossFade()).listener(object:RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent( this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent( this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                return true
            }
        }).into(splashIv)
    }

    override fun onDestroy() {
        super.onDestroy()
        if( disposable.isDisposed){
            disposable.dispose()
        }
    }
}