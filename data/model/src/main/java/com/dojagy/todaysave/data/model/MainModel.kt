package com.dojagy.todaysave.data.model

data class MainModel<T>(
    val state: ApiState = ApiState.DEFAULT,
    val message: String? = null,
    val data: T? = null
)

enum class ApiState {
    DEFAULT,
    SUCCESS,
    FAIL,
    ERROR
}