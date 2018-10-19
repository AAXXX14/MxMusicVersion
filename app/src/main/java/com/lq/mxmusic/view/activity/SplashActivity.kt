package com.lq.mxmusic.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lq.administrator.mxmusic.R
import com.lq.mxmusic.base.BaseActivity
import com.lq.mxmusic.callback.LifeCallBack
import com.lq.mxmusic.callback.SafeClickCallBack
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
class SplashActivity : AppCompatActivity() {
    private lateinit var disposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        forbidShowToolbar()
//        forbidShowBottom()
//        setStatusColor(R.color.transparent)
        val i = Random().nextInt(CacheImageConfig.SPLASH_URLS.size)
        Glide.with(this).load(CacheImageConfig.SPLASH_URLS[i]).listener(object : RequestListener<String, GlideDrawable> {
            override fun onException(e: Exception, model: String?, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            LifeCallBack.remove(this@SplashActivity)
                            finish()
                        }
                return false
            }

            override fun onResourceReady(resource: GlideDrawable, model: String?, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                disposable = Observable.timer(5, TimeUnit.MILLISECONDS).compose(Transformer.switchSchedulers())
                        .subscribe {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            LifeCallBack.remove(this@SplashActivity)
                            finish()

                        }
                return false
            }
        }).into(splashIv)
        jumpOverTv.setOnClickListener(object:SafeClickCallBack(){
            override fun onNoDoubleClick(v: View) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                LifeCallBack.remove(this@SplashActivity)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (disposable.isDisposed) {
            disposable.dispose()
        }
    }
}