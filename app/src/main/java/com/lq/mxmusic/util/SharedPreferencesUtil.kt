import android.content.Context
import com.lq.mxmusic.base.App
import com.lq.mxmusic.reposity.config.AppConfig

object SharedPreferencesUtil {
    private val sharedPreferences by lazy { App.instance.applicationContext.getSharedPreferences(AppConfig.SP_DEFAULT_NAME, Context.MODE_PRIVATE) }

    //当前播放歌单
    fun setPlaySource(source: Int) {
        sharedPreferences.edit().putInt(AppConfig.PLAY_SOURCE, source).apply()
    }

    fun getPlaySource(): Int {
        return sharedPreferences.getInt(AppConfig.PLAY_SOURCE, 1)//默认本地音乐
    }

    //当前播放位置
    fun setPlayPosition(position: Int) {
        sharedPreferences.edit().putInt(AppConfig.PLAY_POSITION, position).apply()
    }

    fun getPlayPosition(): Int {
        return sharedPreferences.getInt(AppConfig.PLAY_POSITION, 0)
    }

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

    fun getDownLoadNumber(): Int {
        return sharedPreferences.getInt(AppConfig.MUSIC_DOWNLOAD_MANAGER_NUMBER, 0)
    }

    //当前播放进度
    fun setCurrentPlayPosition(position: Int) {
        sharedPreferences.edit().putInt(AppConfig.MUSIC_CURRENT_PLAY_POSITION, position).apply()
    }

    fun getCurrentPlayPosition(): Int {
        return sharedPreferences.getInt(AppConfig.MUSIC_CURRENT_PLAY_POSITION, 0)
    }

    //存储当前主题
   fun setCurrentTheme(theme:Int){
        sharedPreferences.edit().putInt(AppConfig.CURRENT_THEME,theme).apply()
    }

    fun getCurrentTheme() :Int{
        return sharedPreferences.getInt(AppConfig.CURRENT_THEME,AppConfig.THEME_DEFAULT)
    }



}