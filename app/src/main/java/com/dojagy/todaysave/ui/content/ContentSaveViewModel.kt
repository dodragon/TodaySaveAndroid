package com.dojagy.todaysave.ui.content

import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.util.onFailure
import com.dojagy.todaysave.common.util.onSuccess
import com.dojagy.todaysave.data.domain.usecase.ContentUseCase
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.content.MetadataModel
import com.dojagy.todaysave.ui.content.ContentSaveEvent.LinkShared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ContentSaveViewModel @Inject constructor(
    private val contentUseCase: ContentUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel<ContentSaveState, ContentSaveEffect, ContentSaveEvent>() {

    private fun requestMetadata(
        url: String
    ) {
        setState { copy(isLoading = true) }
        request {
            contentUseCase.metadata(url)
                .onSuccess {
                    setState {
                        copy(metadata = it, isLoading = false)
                    }
                }
                .onFailure {
                    setState { copy(isLoading = false) }
                }
        }
    }

    override fun createInitialState() = ContentSaveState()

    override fun handleEvent(
        event: ContentSaveEvent
    ) {
        when(event) {
            is LinkShared -> {
                event.link?.let { link ->
                    requestMetadata(link)
                }
                setState {
                    copy(linkShared = event)
                }
            }
        }
    }

    fun showSnackBar(
        message: String
    ) {
        showSnackBar(message = message)
    }

    suspend fun isLogin() = userUseCase.isLogin.first()
}

data class ContentSaveState(
    val linkShared: LinkShared = LinkShared(),
    val metadata: MetadataModel? = null,
    override val isLoading: Boolean = false
) : BaseUiState

sealed interface ContentSaveEvent : BaseUiEvent {
    data class LinkShared(
        val type: LinkSharedType = LinkSharedType.TYPE,
        val link: String? = null
    ) : ContentSaveEvent
}

sealed interface ContentSaveEffect : BaseUiEffect {

}

enum class LinkSharedType {
    APP_SHARE,
    CLIPBOARD,
    TYPE
}