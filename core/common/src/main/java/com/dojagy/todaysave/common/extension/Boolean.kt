package com.dojagy.todaysave.common.extension

fun Boolean?.isTrue(): Boolean = this == true

fun Boolean?.isFalse(): Boolean = this == false

fun Boolean?.isNullOrFalse(): Boolean = this == null || this == false

fun Boolean.formatYn(): String {
    return if(this.isTrue()) {
        "Y"
    }else {
        "N"
    }
}