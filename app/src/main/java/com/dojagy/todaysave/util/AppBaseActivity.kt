package com.dojagy.todaysave.util

import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.ui.auth.login.LoginActivity

abstract class AppBaseActivity<S : BaseUiState, E : BaseUiEffect, V : BaseUiEvent, VM : BaseViewModel<S, E, V>>
    : BaseActivity<S, E, V, VM>() {

    /**
     * 부모(BaseActivity)가 요구한 loginActivityClassName을 여기서 구현해준다.
     */
    override val loginActivityClassName: String
        get() = LoginActivity::class.java.name
}