package com.dojagy.todaysave.ui.content

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.util.onSuccess
import com.dojagy.todaysave.data.domain.usecase.ContentUseCase
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.content.MetadataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentSaveViewModel @Inject constructor(
    private val contentUseCase: ContentUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel<ContentSaveState, ContentSaveEffect, ContentSaveEvent>() {

    init {
        contentSaveStart()
    }

    private fun contentSaveStart() {
        viewModelScope.launch {
            val isLogin = userUseCase.isLogin.first()

            setState {
                copy(
                    isLogin = isLogin
                )
            }
        }
    }

    fun requestMetadata(
        url: String
    ) {
        request {
            contentUseCase.metadata(url)
                .onSuccess {
                    setState {
                        copy(metadata = it)
                    }
                }
        }
    }

    override fun createInitialState() = ContentSaveState()

    override fun handleEvent(
        event: ContentSaveEvent
    ) {
        TODO("Not yet implemented")
    }
}

data class ContentSaveState(
    val isLogin: Boolean = true,
    val sharedUrl: String? = null,
    val clipboardUrl: String? = null,
    val metadata: MetadataModel? = null,
    override val isLoading: Boolean = false
) : BaseUiState

sealed interface ContentSaveEvent : BaseUiEvent {
    //TODO: Events
}

sealed interface ContentSaveEffect : BaseUiEffect {

}