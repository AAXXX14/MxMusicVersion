import android.content.Context
import com.lq.mxmusic.base.App
import com.lq.mxmusic.reposity.config.AppConfig

object SharedPreferencesUtil {
    private val sharedPreferences by lazy { App.instance.applicationContext.getSharedPreferences(AppConfig.SP_DEFAULT_NAME, Context.MODE_PRIVATE) }

    //本地音乐数量
    fun setLocalMusicNumber(number: Int) {
        sharedPreferences.edit().putInt(AppConfig.LOCAL_MUSIC_NUMBER, number).apply()
    }

    fun getLocalMusicNumber(): Int {
        return sharedPreferences.getInt(AppConfig.LOCAL_MUSIC_NUMBER, 0)
    }

    //最近播放音乐数量
    fun setNearlyMusicPlayNumber(number: Int) {
        sharedPreferences.edit().putInt(AppConfig.NEARLY_MUSIC_PLAY_NUMBER, number).apply()
    }

    fun getNearlyMusicPlayNumber(): Int {
        return sharedPreferences.getInt(AppConfig.NEARLY_MUSIC_PLAY_NUMBER, 0)
    }

    //下载管理数量
    fun setDownLoadNumber(number: Int) {
        sharedPreferences.edit().putInt(AppConfig.MUSIC_DOWNLOAD_MANAGER_NUMBER, number).apply()
    }

    fun getDownLoadNumber():Int{
        return sharedPreferences.getInt(AppConfig.MUSIC_DOWNLOAD_MANAGER_NUMBER,0)
    }

    //夜间模式
    fun setDayNightMode(isDay:Boolean){
        sharedPreferences.edit().putBoolean(AppConfig.APP_DAY_NIGHT_MODE,isDay).apply()
    }

    fun getDayNightMode():Boolean{
        return sharedPreferences.getBoolean(AppConfig.APP_DAY_NIGHT_MODE,false)
    }

}