package com.dojagy.todaysave.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.ui.splash.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {


    fun logout() {
        viewModelScope.launch {
            userUseCase.logout()
        }
    }
}