package com.dojagy.todaysave.common.util

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import kotlin.reflect.full.memberProperties

object DLog {

    init {
        Napier.base(DebugAntilog())
    }
    
    var isDebug = true

    fun d(msg: Any?) {
        if(isDebug) {
            Napier.d(msg.toIfDataClass())
        }
    }

    fun d(
        tag: String,
        msg: Any?,
        throwable: Throwable? = null
    ) {
        if(isDebug) {
            Napier.d(
                message = msg.toIfDataClass(),
                throwable = throwable,
                tag = tag
            )
        }
    }

    fun i(
        tag: String,
        msg: Any?,
        throwable: Throwable? = null
    ) {
        if(isDebug) {
            Napier.i(
                message = msg.toIfDataClass(),
                throwable = throwable,
                tag = tag
            )
        }
    }

    fun e(
        tag: String,
        msg: Any?
    ) {
        if(isDebug) {
            Napier.log(LogLevel.ERROR, tag, null, msg.toIfDataClass())
        }
    }

    fun Any?.toIfDataClass(): String {
        val message = if (this != null && this::class.isData) {
            val indent = "   "
            val stringBuilder = StringBuilder()
            val dataClass = this::class
            stringBuilder
                .append(dataClass.simpleName)
                .append("(")
            dataClass.memberProperties.forEach {
                val property = "\"${it.name}\": ${it.call(this)}"
                stringBuilder.append("\n$indent$property,")
            }
            stringBuilder.append("\n)")
            stringBuilder
        } else {
            this
        }
        return message.toString()
    }

}