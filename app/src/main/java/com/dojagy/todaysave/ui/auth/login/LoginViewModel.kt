package com.dojagy.todaysave.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.data.domain.onFailure
import com.dojagy.todaysave.data.domain.onSuccess
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.user.SnsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel<LoginState, LoginEffect, LoginEvent>() {

    init {
        startLoginView()
    }

    fun requestLogin(
        email: String,
        snsKey: String,
        snsType: SnsType,
        nickname: String
    ) {
        setState { copy(isLoading = true) }

        viewModelScope.launch {
            userUseCase.login(
                email = email,
                snsKey = snsKey,
                snsType = snsType
            ).onSuccess {
                postEffect(LoginEffect.LoginSuccess(snsType))
                setState { copy(isLoading = false) }
            }.onFailure { msg ->
                postEffect(LoginEffect.LoginFail(msg, JoinDataModel(
                    email = email,
                    snsType = snsType.name,
                    snsKey = snsKey,
                    nickname = nickname
                )))

                setState { copy(isLoading = false) }
            }
        }
    }

    fun setLastLogin(
        snsType: SnsType
    ) {
        viewModelScope.launch {
            userUseCase.setLastLogin(snsType)
        }
    }

    private fun startLoginView() {
        viewModelScope.launch {
            val lastType = userUseCase.lastLoginType().value
            setState {
                copy(
                    lastLoginType = lastType
                )
            }
        }
    }

    override fun createInitialState() = LoginState()

    override fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                postEffect(LoginEffect.LoginStart(event.type))
            }
        }
    }

    fun loginError(
        msg: String
    ) {
        postEffect(LoginEffect.LoginFail(msg, null))
    }

    fun showErrorSnackBar(
        msg: String
    ) {
        showSnackBar(message = msg)
    }
}

data class LoginState(
    val lastLoginType: SnsType? = null,
    override val isLoading: Boolean = false,
) : BaseUiState

sealed interface LoginEvent : BaseUiEvent {
    data class Login(val type: SnsType) : LoginEvent
}

sealed interface LoginEffect : BaseUiEffect {
    data class LoginSuccess(val type: SnsType) : LoginEffect
    data class LoginFail(val msg: String, val joinData: JoinDataModel?) : LoginEffect
    data class LoginStart(val type: SnsType) : LoginEffect
}

data class JoinDataModel(
    val email: String,
    val snsType: String,
    val snsKey: String,
    val nickname: String
)