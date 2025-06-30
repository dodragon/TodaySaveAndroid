package com.dojagy.todaysave.common.extension

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

val Long.Companion.DEFAULT: Long get() = 0L

fun Long.toWon() = NumberFormat.getNumberInstance().format(this) + " 원"
fun Long.toPoint() = NumberFormat.getNumberInstance().format(this) + " P"

fun Long.divideZeroCheck(divideNum: Long): Long {
    return if (this == 0L) {
        0L
    } else {
        this / divideNum
    }
}

/**
 *  10 미만인 경우 앞에 0 붙여서 반환
 */
fun Long.zeroFormat() = if(this < 10) {
    "0${this}"
}else {
    this.toString()
}

fun Long.defaultDecimalFormat(): String {
    var value = this
    if (this < 0) value *= -1
    return DecimalFormat("###,###,###").format(value)
}

fun Long.countdownTimeFormat(): String {
    val min = this.divideZeroCheck(60)
    val sec = this % 60
    return String.format(Locale.KOREAN, "%02d:%02d", min, sec)
}