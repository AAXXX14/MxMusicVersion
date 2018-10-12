import android.util.Log

import com.google.gson.JsonParseException

import org.json.JSONException

import java.net.ConnectException

import retrofit2.HttpException

class ExceptionHandle {


    /**
     * 约定异常
     */
    internal object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005  //用于https 请求
    }

    class ResponseThrowable(throwable: Throwable, var code: Int) : Exception(throwable) {
        override var message: String? = null
    }

    /**
     * ServerException发生后，将自动转换为ResponeThrowable返回
     */
    internal inner class ServerException : RuntimeException() {
        var code: Int = 0
        override var message: String? = null
    }

    companion object {
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        fun handleException(e: Throwable): ResponseThrowable {
            val ex: ResponseThrowable
            Log.w("ExceptionHandle", "e.toString = " + e.toString())
            if (e is HttpException) {
                ex = ResponseThrowable(e, ERROR.HTTP_ERROR)
                when (e.code()) {
                    UNAUTHORIZED -> ex.message = "服务器未授权"
                    FORBIDDEN -> ex.message = "服务器拒绝访问"
                    NOT_FOUND -> ex.message = "服务器开小差了"
                    REQUEST_TIMEOUT -> ex.message = "服务器请求超时"
                    INTERNAL_SERVER_ERROR -> ex.message = "服务器故障500"
                    BAD_GATEWAY -> ex.message = "服务器故障502"
                    SERVICE_UNAVAILABLE -> ex.message = "服务器维护中"
                    GATEWAY_TIMEOUT -> ex.message = "服务器超时"
                    else -> ex.message = "网络错误"
                }
                return ex
            } else if (e is ServerException) {
                ex = ResponseThrowable(e, e.code)
                ex.message = e.message
                return ex
            } else if (e is JsonParseException || e is JSONException) {
                ex = ResponseThrowable(e, ERROR.PARSE_ERROR)
                ex.message = "解析错误"
                return ex
            } else if (e is ConnectException) {
                ex = ResponseThrowable(e, ERROR.NETWORK_ERROR)
                ex.message = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ResponseThrowable(e, ERROR.SSL_ERROR)
                ex.message = "证书验证失败"
                return ex
            } else {
                ex = ResponseThrowable(e, ERROR.UNKNOWN)
                ex.message = "未知错误"
                return ex
            }
        }
    }
}
