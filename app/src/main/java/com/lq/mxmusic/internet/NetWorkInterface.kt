interface NetWorkInterface<T> {
    fun onSuccess(t: T)

    fun onError(message: String)
}
