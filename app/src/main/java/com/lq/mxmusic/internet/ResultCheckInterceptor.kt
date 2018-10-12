import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class ResultCheckInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
//                .addHeader("Connection", "close")
                .addHeader("Accept", "*/*")
                .addHeader("Cookie", "add cookies here")
                .build()
        return chain.proceed(request)

    }
}
