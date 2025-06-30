package com.dojagy.todaysave.common.extension

val Double.Companion.DEFAULT: Double get() = 0.0

fun Double?.defaultLatitude(): Double = if (this == null || this == 0.0) 37.566295 else this
fun Double?.defaultLongitude(): Double = if (this == null || this == 0.0) 126.977945 else this

fun Double.divideZeroCheck(divideNum: Double): Double {
    return if (this == 0.0) {
        0.0
    } else {
        this / divideNum
    }
}