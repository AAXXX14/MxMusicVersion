
object BaseRetrofit {


    fun <T> create(clazz: Class<T>): T {
        return BaseNetCore.create(clazz)
    }

    fun createAPI(): APIService {
        return BaseNetCore.create(APIService::class.java)
    }

    /**
     * COOKIE的操作类
     */
    enum class COOKIE {
        INSTANCE;
    }
}

