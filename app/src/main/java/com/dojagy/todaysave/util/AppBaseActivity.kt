package com.dojagy.todaysave.util

import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.data.view.AuthEventHandler
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class AppBaseActivity<S: BaseUiState, E: BaseUiEffect, V: BaseUiEvent, VM : BaseViewModel<S, E, V>> : BaseActivity<S, E, V, VM>() {

    @Inject
    lateinit var hiltAuthEventHandler: HiltAuthEventHandler

    // BaseActivity가 요구한 AuthEventHandler를 여기서 제공
    override val authEventHandler: AuthEventHandler
        get() = hiltAuthEventHandler

    // BaseActivity가 요구한 로그인 화면 클래스 이름을 여기서 제공
    override val loginActivityClassName: String
        get() = LoginActivity::class.java.name
}