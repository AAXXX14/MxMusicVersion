package com.lq.mxmusic.view.activity

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.reposity.config.CacheImageConfig
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

/*
*2018/10/8 0008  15:14
*闪屏页 _ 启动页 by liu
*/
class SplashActivity : BaseActivity() {
    private lateinit var disposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        forbidShowToolbar()
        forbidShowBottom()
        val i = Random().nextInt(CacheImageConfig.SPLASH_URLS.size)
        Glide.with(this).load(R.drawable.default_bg).listener(object : RequestListener<Int, GlideDrawable> {
            override fun onException(e: Exception, model: Int?, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                return false
            }

            override fun onResourceReady(resource: GlideDrawable, model: Int?, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                return false
            }
        }).into(splashIv)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (disposable.isDisposed) {
            disposable.dispose()
        }
    }
}