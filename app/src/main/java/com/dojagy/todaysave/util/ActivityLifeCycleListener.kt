package com.dojagy.todaysave.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.dojagy.todaysave.common.util.DLog

class ActivityLifeCycleListener : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {

    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        DLog.e("Current Activity", activity::class.java.simpleName)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {}

    override fun onActivityDestroyed(activity: Activity) {}
}