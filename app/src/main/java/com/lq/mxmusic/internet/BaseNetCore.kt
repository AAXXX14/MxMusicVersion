import com.lq.mxmusic.reposity.config.AppConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseNetCore {
    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)

    private lateinit var okHttpClient: OkHttpClient

    fun initRetrofit() {
        HttpLoggingInterceptor().level = HttpLoggingInterceptor.Level.BODY
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ResultCheckInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        okHttpClient.dispatcher().maxRequestsPerHost = 16 // 每个主机最大请求数为16
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

}