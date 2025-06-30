package com.dojagy.todaysave.common.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

val String.Companion.DEFAULT: String get() = ""

fun String.fullTrim() = trim().replace("\uFEFF", "")

fun String.doDayOfWeek(format: String = "yyyy-MM-dd"): String? {
    val formatter = SimpleDateFormat(format, Locale.KOREA)
    val cal: Calendar = Calendar.getInstance()
    cal.time = try {
        formatter.parse(this) ?: Date()
    }catch (e: Exception) {
        Date()
    }
    var strWeek: String? = null

    when (cal.get(Calendar.DAY_OF_WEEK)) {
        1 -> {
            strWeek = "일"
        }

        2 -> {
            strWeek = "월"
        }

        3 -> {
            strWeek = "화"
        }

        4 -> {
            strWeek = "수"
        }

        5 -> {
            strWeek = "목"
        }

        6 -> {
            strWeek = "금"
        }

        7 -> {
            strWeek = "토"
        }
    }
    return strWeek
}

fun String?.toSeq() = if (!this.isNullOrEmpty()) {
    try {
        this.toLong()
    } catch (e: NumberFormatException) {
        0L
    }
} else {
    0L
}

fun String?.toSeqOrNull() = if (!this.isNullOrEmpty()) {
    try {
        this.toLong()
    } catch (e: NumberFormatException) {
        null
    }
} else {
    null
}

fun String?.ynToBoolean() = this?.uppercase() == "Y"

fun String.timeFormat() = this.padStart(2, '0')

fun String.containList(
    compareText: String
): List<Int> {
    val indexList = mutableListOf<Int>()
    val lastIndex = this.lastIndexOf(compareText)
    if(lastIndex < 0 && this.indexOf(compareText) < 0) {
        return indexList
    }else {
        for(i in this.indices) {
            val index = this.indexOf(compareText, i)
            if(indexList.contains(index).isFalse() && index >= 0) {
                indexList.add(index)
            }
        }
    }

    return indexList
}

fun String.toDateFormat(
    format: String,
    toFormat: String
): String {
    return try {
        val formatter = SimpleDateFormat(format, Locale.KOREA)
        SimpleDateFormat(toFormat, Locale.KOREA)
            .format(formatter.parse(this))
    }catch (e: Exception) {
        e.printStackTrace()
        String.DEFAULT
    }
}

fun String.toKoreanTime(
    format: String
): String {
    val formatter = SimpleDateFormat(format, Locale.KOREA)
    val date = formatter.parse(this) ?: Date()
    val cal = Calendar.getInstance()
    cal.time = date
    return "${if(cal.get(Calendar.AM_PM) > 0) "오후" else "오전"} " +
            "${(if(cal.get(Calendar.HOUR_OF_DAY) > 12) cal.get(Calendar.HOUR_OF_DAY) - 12 else cal.get(Calendar.HOUR_OF_DAY)).toString().timeFormat()}:" +
            cal.get(Calendar.MINUTE).toString().timeFormat()
}

fun String.phoneNumberCheck(): Boolean {
    val pattern = "^01([0|16789])-?([0-9]{4})-?([0-9]{4})$"
    return Pattern.matches(pattern, this)
}