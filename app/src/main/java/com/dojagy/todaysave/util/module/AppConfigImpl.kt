package com.dojagy.todaysave.util.module

import com.dojagy.todaysave.BuildConfig
import com.dojagy.todaysave.common.util.AppConfig

class AppConfigImpl : AppConfig {
    override val baseUrl: String = if(BuildConfig.DEBUG) {
        "http://192.168.0.7:8083/"
    }else {
        "realUrl"
    }
}