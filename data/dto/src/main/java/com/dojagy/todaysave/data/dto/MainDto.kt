package com.dojagy.todaysave.data.dto

data class MainDto<T>(
    val status: String?,
    val message: String?,
    val data: T?
)