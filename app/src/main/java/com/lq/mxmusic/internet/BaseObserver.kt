import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by 刘清
 * on 2018/7/4.
 * on 17:17
 * function 类 名称 作用: 对数据进行过滤
 */

abstract class BaseObserver<T> : Observer<ResultEntity<T>> {
    override fun onSubscribe(d: Disposable) {}

    override fun onNext(result: ResultEntity<T>) {
        if (result.status == 1) {
            onFail(result.message!!)
        } else {
            onSuccess(result.data!!)
        }
    }

    override fun onError(e: Throwable) {
        onFail(ExceptionHandle.handleException(e).message + "")
    }

    override fun onComplete() {}

    protected abstract fun onSuccess(t: T)

    protected abstract fun onFail(message: String)
}
