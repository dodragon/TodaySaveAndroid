package com.dojagy.todaysave.ui.splash

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.android.base.DismissType
import com.dojagy.todaysave.common.android.base.SnackBarType
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel<SplashState, SplashEffect, SplashEvent>() {

    init {
        startSplash()
    }

    private fun startSplash() {
        viewModelScope.launch {
            val appCheckResultDeferred = async { requestAppCheck() }
            delay(2000)

            val isUpdateRequired = appCheckResultDeferred.await()
            if (isUpdateRequired) {
                setState { copy(updateRequired = true) }
                return@launch
            }

            // 3. 로그인 상태 확인
            val isLogin = userUseCase.isLogin.first()

            DLog.e("isLogin", isLogin)

            // 4. 최종 목적지로 이동
            if (isLogin.isTrue()) {
                postEffect(SplashEffect.NavigateMain)
            } else {
                postEffect(SplashEffect.NavigateLogin)
            }
        }
    }

    private suspend fun requestAppCheck(): Boolean {
        //TODO: 앱 버전 체크

        /*showSnackBar(
            title = "",
            message = "",
            type = SnackBarType.Normal,
            dismissType = DismissType.Action<SplashEvent.GoUpdate>(
                actionLabel = "업데이트",
                event = SplashEvent.GoUpdate
            )
        )*/

        return false
    }

    override fun createInitialState() = SplashState()

    override fun handleEvent(
        event: SplashEvent
    ) {
        when (event) {
            is SplashEvent.GoUpdate -> {
                postEffect(SplashEffect.NavigateUpdate)
            }
        }
    }
}

data class SplashState(
    val updateRequired: Boolean = false,
    override val isLoading: Boolean = false
) : BaseUiState

sealed interface SplashEvent : BaseUiEvent {
    data object GoUpdate : SplashEvent
}

sealed interface SplashEffect : BaseUiEffect {
    data object NavigateUpdate : SplashEffect
    data object NavigateLogin : SplashEffect
    data object NavigateMain : SplashEffect
}