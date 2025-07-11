package com.dojagy.todaysave.common.android.util

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray1
import com.dojagy.todaysave.core.resources.theme.Gray7

object CustomTabHelper {

    fun openUrl(
        context: Context,
        url: String,
        onError: () -> Unit = {}
    ) {
        val uri = url.toUri()

        val closeButtonIcon: Bitmap? = createTintedBitmap(
            context = context,
            drawableResId = R.drawable.icon_arrow_back,
            colorInt = Gray7.toArgb()
        )

        try {
            val defaultParams = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(Gray1.toArgb())
                .build()

            val builder = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(defaultParams)
                .setShowTitle(false)
                .setUrlBarHidingEnabled(false)

            closeButtonIcon?.let {
                builder.setCloseButtonIcon(it)
            }

            builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF)

            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, uri)
        }catch (e: Exception) {
            e.printStackTrace()
            onError()
        }
    }

    private fun createTintedBitmap(
        context: Context,
        @DrawableRes
        drawableResId: Int,
        @ColorInt
        colorInt: Int
    ): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableResId) ?: return null
        val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
        DrawableCompat.setTint(wrappedDrawable, colorInt)
        return wrappedDrawable.toBitmap()
    }
}