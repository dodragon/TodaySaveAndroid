package com.dojagy.todaysave.common.extension

import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.pow
import kotlin.math.roundToInt

val Int.Companion.DEFAULT: Int get() = 0

fun Int.toWon() = NumberFormat.getNumberInstance().format(this) + " 원"
fun Int.toPoint() = NumberFormat.getNumberInstance().format(this) + " P"

fun Int.toKmOrMeter(): String {
    return if(this >= 1000) {
        "${this.divideZeroCheck(1000)}.${(this%1000).toString().first()} km"
    }else {
        "$this m"
    }
}

fun Int.divideZeroCheck(divideNum: Int): Int {
    return if (this == 0) {
        0
    } else if(divideNum == 0) {
        this
    }else {
        this / divideNum
    }
}

fun Int.defaultDecimalFormat(): String {
    var value = this
    if (this < 0) value *= -1
    return DecimalFormat("###,###,###").format(value)
}

fun Int.round(
    //자리수 : 1 = 1, 2 = 10, 3 = 100...
    digit: Int
): Int {
    if (digit == 0) {
        return this
    }else if(this == 0) {
        return 0
    }

    val divisor = 10.0.pow(digit)
    return (this / divisor).roundToInt() * divisor.toInt()
}

fun Int.dateZeroFormat(): String {
    return if(this < 10) {
        "0${this}"
    }else {
        this.toString()
    }
}