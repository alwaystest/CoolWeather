package com.software.eric.coolweather.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

object FileLogger {

    private val file by lazy {
        File(MyApplication.context.cacheDir, "log")
    }

    fun log(tag: String, msg: String) {
        GlobalScope.launch(Dispatchers.IO) {
            synchronized(file) {
                file.appendText("$tag\t$msg\n")
            }
        }
    }
}
