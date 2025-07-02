package com.dojagy.todaysave.data.source.api.interceptor

import com.dojagy.todaysave.common.util.DLog
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class PrettyHttpLoggingInterceptor : HttpLoggingInterceptor.Logger {

    companion object {
        private const val HTTP_LOG_TAG = "okHttpClient"
    }

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(JsonParser.parseString(message))
                DLog.i(HTTP_LOG_TAG, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                DLog.d(HTTP_LOG_TAG, message)
            }
        } else {
            DLog.d(HTTP_LOG_TAG, message)
            return
        }
    }
}