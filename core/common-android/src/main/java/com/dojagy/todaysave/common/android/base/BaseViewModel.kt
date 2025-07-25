package com.dojagy.todaysave.common.android.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.util.Return
import com.dojagy.todaysave.common.util.onInvalidToken
import com.dojagy.todaysave.core.resources.wrapper.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseUiState, E : BaseUiEffect, V : BaseUiEvent> : ViewModel() {

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(createInitialState()) }
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<BaseUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    protected abstract fun createInitialState(): S

    /**
     * 사용자 이벤트를 처리하는 공통 진입점
     */
    abstract fun handleEvent(event: V)

    protected fun setState(reduce: S.() -> S) {
        _uiState.value = uiState.value.reduce()
    }

    protected fun postEffect(effect: BaseUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }

    protected fun <T> request(
        method: suspend CoroutineScope.() -> Return<T>
    ) {
        viewModelScope.launch {
            method.invoke(this)
                .onInvalidToken {
                    postEffect(BaseUiEffect.InvalidToken)
                }
        }
    }

    protected fun showSnackBar(
        title: UiText? = null,
        message: UiText,
        type: SnackBarType = SnackBarType.Normal,
        dismissType: DismissType<V> = DismissType.Automatic
    ) {
        postEffect(BaseUiEffect.ShowSnackBar<V>(
            snackBarData = SnackBarMessage<V>(
                title = title,
                message = message,
                type = type,
                dismissType = dismissType
            )
        ))
    }
}