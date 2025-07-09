package com.dojagy.todaysave.ui.auth.join

import androidx.lifecycle.viewModelScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.extension.isFalse
import com.dojagy.todaysave.data.domain.onFailure
import com.dojagy.todaysave.data.domain.onSuccess
import com.dojagy.todaysave.data.domain.usecase.UserUseCase
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.ui.auth.join.JoinEffect.*
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
                    text = "(필수) 만 14세 이상",
                    title = "14세 이상",
                    type = CheckTermType.AGE,
                    url = null
                ),
                isCheck = false
            ),
            TermsCheck(
                term = Term(
                    text = "(필수) 개인정보 처리방침 동의",
                    title = "개인정보 처리방침",
                    type = CheckTermType.PERSONAL,
                    url = "https://celestial-face-615.notion.site/22a2a7cac6a180cda6c7fce8c6ecbde1?source=copy_link"
                ),
                isCheck = false
            ),
            TermsCheck(
                term = Term(
                    text = "(필수) 이용약관 동의",
                    title = "이용약관",
                    type = CheckTermType.USABLE,
                    url = "https://celestial-face-615.notion.site/22a2a7cac6a180028cc1e7530961172c?source=copy_link"
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
                            randomNickname = it,
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
                            selectedNickname = nickname
                        )
                    }
                }.onFailure {
                    setState {
                        copy(
                            nicknameErrorMessage = "이미 있는 닉네임이에요. 다른 닉네임을 입력해 주세요."
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
                showSnackBar(message = msg)
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
                        showSnackBar(message = "${it.term.title}에 동의해주세요.")
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
                            nicknameErrorMessage = "닉네임을 입력해 주세요"
                        )
                    }
                }else if(Regex("^[a-zA-Z0-9가-힣]*$").matches(inputNickname).isFalse()) {
                    setState {
                        copy(
                            nicknameErrorMessage = "닉네임에는 특수문자나 공백을 포함할 수 없습니다"
                        )
                    }
                }else if(inputNickname.length < 2 || inputNickname.length > 12) {
                    setState {
                        copy(
                            nicknameErrorMessage = "닉네임은 2~12자 이내로 작성해주세요"
                        )
                    }
                }else {
                    requestNicknameCheck(inputNickname)
                }
            }
            is JoinEvent.OnNicknameChange -> {
                setState {
                    copy(
                        selectedNickname = String.DEFAULT,
                        isNicknameChecked = false,
                        nicknameErrorMessage = String.DEFAULT,
                        randomNickname = String.DEFAULT
                    )
                }
            }
            is JoinEvent.OnTermsSheetDismissed -> {
                setState {
                    copy(
                        isNicknameChecked = false,
                        selectedNickname = String.DEFAULT
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
    val text: String,
    val title: String,
    val type: CheckTermType,
    val url: String?
)

data class JoinState(
    val randomNickname: String = String.DEFAULT,
    val selectedNickname: String = String.DEFAULT,
    val isNicknameChecked: Boolean = false,
    val nicknameErrorMessage: String = String.DEFAULT,
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