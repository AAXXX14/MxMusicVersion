import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.Toast
import es.dmoral.toasty.Toasty

object ShowUtils  {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showWarning(context: Context, message: String) {
        Toasty.warning(context.applicationContext, message, Toast.LENGTH_SHORT, true).show()
    }

    fun showSuccess(context: Context, message: String) {
        Toasty.success(context.applicationContext, message, Toast.LENGTH_SHORT, true).show()
    }

    fun showInfo(context: Context, message: String) {
        Toasty.info(context.applicationContext, message, Toast.LENGTH_SHORT, true).show()
    }


    fun showError(context: Context, message: String) {
        Toasty.error(context.applicationContext, message, Toast.LENGTH_SHORT, true).show()
    }





}