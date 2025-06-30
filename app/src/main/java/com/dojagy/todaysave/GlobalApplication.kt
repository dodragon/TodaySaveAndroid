package com.dojagy.todaysave

import android.app.Application
import com.dojagy.todaysave.common.util.DLog
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DLog.isDebug = BuildConfig.DEBUG
    }
}