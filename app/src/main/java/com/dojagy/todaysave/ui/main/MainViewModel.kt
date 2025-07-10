package com.dojagy.todaysave.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.data.domain.onSuccess
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.user.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    init {
        user()
    }

    fun logout() {
        viewModelScope.launch {
            userUseCase.logout()
        }
    }

    fun user() {
        viewModelScope.launch {
            userUseCase.myInfo().collect {
                it.onSuccess {
                    _user.value = it
                }
            }
        }
    }
}