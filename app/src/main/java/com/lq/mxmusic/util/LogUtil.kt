package com.lq.mxmusic.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/*
*2018/10/12 0012  10:21
*function by lq
*/
object LogUtil{
    private const val TOP_BORDER = "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER = "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val CHUNK_SIZE = 106 //设置字节数
    private var debug: Boolean = true//是否打印log
    private var saved: Boolean = false//是否存log到sd卡
    private var logDir = ""//设置文件存储目录
    private var logSize = 2 * 1024 * 1024L//设置log文件大小 k



    fun v(tag: String = "www.hotapk.cn_log", msg: String) = debug.debugLog(tag, msg, Log.VERBOSE)
    fun d(tag: String = "www.hotapk.cn_log", msg: String) = debug.debugLog(tag, msg, Log.DEBUG)
    fun i(tag: String = "www.hotapk.cn_log", msg: String) = debug.debugLog(tag, msg, Log.INFO)
    fun w(tag: String = "www.hotapk.cn_log", msg: String) = debug.debugLog(tag, msg, Log.WARN)
    fun e(tag: String = "www.hotapk.cn_log", msg: String) = debug.debugLog(tag, msg, Log.ERROR)


    private fun Boolean.debugLog(tag: String, msg: String, type: Int) {
        if (!this) {
            return
        }
        val newMsg = msgFormat(msg)
//        saved.saveToSd("${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}\n${targetStackTraceMSg()}", msg)
        when (type) {
            Log.VERBOSE -> Log.v(tag, newMsg)
            Log.DEBUG -> Log.d(tag, newMsg)
            Log.INFO -> Log.i(tag, newMsg)
            Log.WARN -> Log.w(tag, newMsg)
            Log.ERROR -> Log.e(tag, newMsg)
        }
    }

    private fun msgFormat(msg: String): String {
        val bytes: ByteArray = msg.toByteArray()
        val length = bytes.size
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var newMsg = "$TOP_BORDER\n$LEFT_BORDER\t${sdf.format(Date())}\n$LEFT_BORDER\t${targetStackTraceMSg()}"
        if (length > CHUNK_SIZE) {
            var i = 0
            while (i < length) {
                val count = Math.min(length - i, CHUNK_SIZE)
                val tempStr = String(bytes, i, count)
                newMsg += "\n$LEFT_BORDER\t$tempStr"
                i += CHUNK_SIZE
            }
        } else {
            newMsg += "\n$LEFT_BORDER\t$msg"
        }
        newMsg += "\n$BOTTOM_BORDER"
        return newMsg
    }

    private fun targetStackTraceMSg(): String {
        val targetStackTraceElement = getTargetStackTraceElement()
        return if (targetStackTraceElement != null) {
            "at ${targetStackTraceElement.className}.${targetStackTraceElement.methodName}(${targetStackTraceElement.fileName}:${targetStackTraceElement.lineNumber})"
        } else {
            ""
        }
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            val isLogMethod = stackTraceElement.className == LogUtil::class.java.name
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement
                break
            }
            shouldTrace = isLogMethod
        }
        return targetStackTrace
    }



    /**
     * 是否打印log输出
     * @param debug
     */
    fun debug(debug: Boolean): LogUtil {
        LogUtil.debug = debug
        return this
    }

    /**
     * 是否保存到sd卡
     * @param savesd
     */
    fun saveSd(savesd: Boolean): LogUtil {
        LogUtil.saved = savesd
        return this
    }

    /**
     * 设置每个log的文件大小
     * @param logSize 文件大小 byte
     */
    fun logSize(logSize: Long): LogUtil {
        LogUtil.logSize = logSize
        return this

    }

    /**
     * 设置log文件目录
     * @param logDir 文件目录
     */
    fun logDir(logDir: String): LogUtil {
        LogUtil.logDir = logDir
        return this
    }



}