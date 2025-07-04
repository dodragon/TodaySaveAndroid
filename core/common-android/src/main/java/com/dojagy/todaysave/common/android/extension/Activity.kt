package com.dojagy.todaysave.common.android.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.net.toUri

fun Activity.goMarket(
    packageName: String
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}