package com.dojagy.todaysave.common.android

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    sealed interface UiEvent {
        data class DataSuccess<T>(val data: T?) : UiEvent
        data object Loading : UiEvent
    }

    sealed interface UiEffect {
        data class Error(val msg: String?) : UiEffect
        data object InvalidTokenError : UiEffect
        data class NeedToast(val msg: String) : UiEffect
    }
}