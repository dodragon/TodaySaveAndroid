package com.dojagy.todaysave.ui.auth.join

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.extension.isFalse
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.wrapper.UiText
import com.dojagy.todaysave.common.util.onFailure
import com.dojagy.todaysave.common.util.onSuccess
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.ui.auth.join.JoinEffect.StartOnboard
import com.dojagy.todaysave.ui.auth.join.JoinEffect.StartTermsWeb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel<JoinState, JoinEffect, JoinEvent>() {

    private val _terms = MutableStateFlow<List<TermsCheck>>(
        listOf(
            TermsCheck(
                term = Term(
                    text = UiText.StringResource(R.string.age_term_text),
                    title = UiText.StringResource(R.string.age_term_title),
                    type = CheckTermType.AGE,
                    url = null
                ),
                isCheck = false
            ),
            TermsCheck(
                term = Term(
                    text = UiText.StringResource(R.string.personal_term_text),
                    title = UiText.StringResource(R.string.personal_term_title),
                    type = CheckTermType.PERSONAL,
                    url = UiText.StringResource(R.string.personal_term_url)
                ),
                isCheck = false
            ),
            TermsCheck(
                term = Term(
                    text = UiText.StringResource(R.string.usable_term_text),
                    title = UiText.StringResource(R.string.usable_term_title),
                    type = CheckTermType.USABLE,
                    url = UiText.StringResource(R.string.usable_term_url)
                ),
                isCheck = false
            )
        )
    )
    val terms = _terms.asStateFlow()

    init {
        requestRandomNickname()
    }

    private fun requestRandomNickname() {
        setState {
            copy(isLoading = true)
        }

        viewModelScope.launch {
            userUseCase.randomNickname()
                .onSuccess {
                    setState {
                        copy(
                            randomNickname = UiText.DynamicString(it),
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun requestNicknameCheck(
        nickname: String
    ) {
        viewModelScope.launch {
            userUseCase.checkNickname(nickname)
                .onSuccess {
                    setState {
                        copy(
                            isNicknameChecked = true,
                            selectedNickname = UiText.DynamicString(nickname)
                        )
                    }
                }.onFailure {
                    setState {
                        copy(
                            nicknameErrorMessage = UiText.StringResource(R.string.join_nickname_duplicate_error)
                        )
                    }
                }
        }
    }

    private fun requestJoin(
        email: String,
        snsType: String,
        snsKey: String,
        nickname: String
    ) {
        viewModelScope.launch {
            userUseCase.join(
                email = email,
                snsType = SnsType.valueOf(snsType),
                snsKey = snsKey,
                nickname = nickname
            ).onSuccess {
                postEffect(StartOnboard)
            }.onFailure { msg ->
                showSnackBar(message = UiText.DynamicString(msg))
            }
        }
    }

    override fun createInitialState(): JoinState = JoinState()

    override fun handleEvent(
        event: JoinEvent
    ) {
        when (event) {
            is JoinEvent.OnClickTermsCheck -> {
                val newList = if(event.type == CheckTermType.ALL) {
                    val newCheckState = event.isCheck
                    _terms.value.map { it.copy(isCheck = newCheckState) }
                } else {
                    _terms.value.map { termsCheckItem ->
                        if (termsCheckItem.term.type == event.type) {
                            termsCheckItem.copy(isCheck = event.isCheck)
                        } else {
                            termsCheckItem
                        }
                    }
                }

                _terms.value = newList
            }
            is JoinEvent.OnClickJoin -> {
                terms.value.map {
                    if(it.isCheck.isFalse()) {
                        showSnackBar(message = UiText.StringResource(R.string.terms_check_error_message, it.term.title))
                        return@handleEvent
                    }
                }

                requestJoin(
                    email = event.email,
                    snsType = event.snsType,
                    snsKey = event.snsKey,
                    nickname = event.nickname
                )
            }
            is JoinEvent.OnClickShowTerm -> {
                postEffect(StartTermsWeb(event.term))
            }
            is JoinEvent.OnClickNicknameCheck -> {
                val inputNickname = event.nickname
                if(inputNickname.isBlank()) {
                    setState {
                        copy(
                            nicknameErrorMessage = UiText.StringResource(R.string.nickname_empty_error_message)
                        )
                    }
                }else if(Regex("^[a-zA-Z0-9가-힣]*$").matches(inputNickname).isFalse()) {
                    setState {
                        copy(
                            nicknameErrorMessage = UiText.StringResource(R.string.nickname_regex_error_message)
                        )
                    }
                }else if(inputNickname.length < 2 || inputNickname.length > 12) {
                    setState {
                        copy(
                            nicknameErrorMessage = UiText.StringResource(R.string.nickname_length_error_message)
                        )
                    }
                }else {
                    requestNicknameCheck(inputNickname)
                }
            }
            is JoinEvent.OnNicknameChange -> {
                setState {
                    copy(
                        selectedNickname = UiText.DynamicString(String.DEFAULT),
                        isNicknameChecked = false,
                        nicknameErrorMessage = UiText.DynamicString(String.DEFAULT),
                        randomNickname = UiText.DynamicString(String.DEFAULT)
                    )
                }
            }
            is JoinEvent.OnTermsSheetDismissed -> {
                setState {
                    copy(
                        isNicknameChecked = false,
                        selectedNickname = UiText.DynamicString(String.DEFAULT)
                    )
                }
            }
        }
    }
}

data class TermsCheck(
    val term: Term,
    val isCheck: Boolean = false
)

data class Term(
    val text: UiText,
    val title: UiText,
    val type: CheckTermType,
    val url: UiText?
)

data class JoinState(
    val randomNickname: UiText = UiText.DynamicString(String.DEFAULT),
    val selectedNickname: UiText = UiText.DynamicString(String.DEFAULT),
    val isNicknameChecked: Boolean = false,
    val nicknameErrorMessage: UiText = UiText.DynamicString(String.DEFAULT),
    override val isLoading: Boolean = false
) : BaseUiState

sealed interface JoinEvent : BaseUiEvent {
    data object OnNicknameChange : JoinEvent
    data class OnClickNicknameCheck(val nickname: String) : JoinEvent
    data class OnClickTermsCheck(
        val type: CheckTermType,
        val isCheck: Boolean
    ) : JoinEvent
    data class OnClickShowTerm(
        val term: Term
    ) : JoinEvent
    data class OnClickJoin(
        val email: String,
        val snsType: String,
        val snsKey: String,
        val nickname: String
    ) : JoinEvent
    data object OnTermsSheetDismissed : JoinEvent
}

enum class CheckTermType{
    ALL,
    AGE,
    PERSONAL,
    USABLE
}

sealed interface JoinEffect : BaseUiEffect {
    data object StartOnboard : JoinEffect
    data class StartTermsWeb(val term: Term) : JoinEffect
}