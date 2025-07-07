package com.dojagy.todaysave.ui.splash

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
            coroutineScope {
                delay(2000)

                // 3. 로그인 상태 확인
                val isLogin = userUseCase.isLogin.first()

                // 4. 최종 목적지로 이동
                if (isLogin.isTrue()) {
                    postEffect(SplashEffect.NavigateMain)
                } else {
                    //TODO: 온보딩 체크
                    //온보딩 체크해서 최초 온보딩 실행 여부 판단
                    postEffect(SplashEffect.NavigateLogin)
                }
            }
        }
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