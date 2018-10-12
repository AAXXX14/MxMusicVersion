import android.util.Log

/**
 * Created by 刘清
 * on 2018/6/11.
 * on 11:00
 * function 类 名称 作用: 返回结果通过此数据过滤。
 */

class ResultEntity<T> {
    var status: Int = 0
    var data: T? = null
        get() {
            Log.d("ResultEntity", "getData: " + field!!.toString())
            return field
        }
    var message: String? = null //errorMSG;

}
