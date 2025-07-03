package com.dojagy.todaysave

import android.app.Application
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.util.ActivityLifeCycleListener
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DLog.isDebug = BuildConfig.DEBUG
        registerActivityLifecycleCallbacks(ActivityLifeCycleListener())

        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))

        // Firebase 초기화
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = BuildConfig.DEBUG.not()
        }
    }
}