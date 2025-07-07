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
                    title = "개인정보 처리방침",
                    url = ""
                ),
                isCheck = false
            ),
            TermsCheck(
                term = Term(
                    title = "이용약관",
                    url = ""
                ),
                isCheck = false
            )
        )
    )
    val terms = _terms.asStateFlow()

    private fun requestNicknameCheck(
        nickname: String
    ) {
        viewModelScope.launch {
            userUseCase.checkNickname(nickname)
                .onSuccess {
                    setState {
                        copy(
                            isNicknameChecked = true,
                            currentRoute = JoinRoute.TERMS
                        )
                    }
                }.onFailure { msg ->
                    showSnackBar(message = msg)
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
            is JoinEvent.OnCheckedTerm -> {
                val newList = _terms.value.map { termsCheckItem ->
                    if (termsCheckItem.term.title == event.termsCheck.term.title) {
                        termsCheckItem.copy(isCheck = event.termsCheck.isCheck)
                    } else {
                        termsCheckItem
                    }
                }
                // 새로 생성된 리스트를 할당해야 StateFlow가 변경을 감지함
                _terms.value = newList
            }

            is JoinEvent.OnBack -> {
                when(uiState.value.currentRoute) {
                    JoinRoute.NICKNAME -> {}
                    JoinRoute.TERMS -> {
                        setState {
                            copy(
                                isNicknameChecked = true,
                                currentRoute = JoinRoute.NICKNAME
                            )
                        }
                    }
                }
            }
            is JoinEvent.OnJoinButtonClick -> {
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
            is JoinEvent.OnNextButtonClick -> {
                if(uiState.value.isNicknameChecked) {
                    setState { copy(currentRoute = JoinRoute.TERMS) }
                }else {
                    requestNicknameCheck(uiState.value.inputNickname)
                }
            }
            is JoinEvent.OnNicknameChange -> {
                setState {
                    copy(
                        inputNickname = event.nickname,
                        isNicknameChecked = false
                    )
                }
            }
            is JoinEvent.OnClickShowTerm -> {
                postEffect(StartTermsWeb(event.term))
            }
        }
    }

    fun showDefaultSnackBar(
        message: String
    ) {
        showSnackBar(message = message)
    }
}

data class TermsCheck(
    val term: Term,
    val isCheck: Boolean = false
)

data class Term(
    val title: String,
    val url: String
)

data class JoinState(
    val currentRoute: JoinRoute = JoinRoute.NICKNAME, // 현재 화면이 어디인지
    val inputNickname: String = String.DEFAULT, // 닉네임 상태를 ViewModel이 직접 관리
    val isNicknameChecked: Boolean = false, // API 호출 성공 여부만 저장
    override val isLoading: Boolean = false
) : BaseUiState

sealed interface JoinEvent : BaseUiEvent {
    data class OnNicknameChange(val nickname: String) : JoinEvent // 닉네임 입력 이벤트
    data object OnNextButtonClick : JoinEvent // 하단 버튼 클릭 이벤트 (역할이 명확해짐)
    data class OnJoinButtonClick(
        val email: String,
        val snsType: String,
        val snsKey: String,
        val nickname: String
    ) : JoinEvent
    data class OnCheckedTerm(
        val termsCheck: TermsCheck,
    ) : JoinEvent
    data class OnClickShowTerm(
        val term: Term
    ) : JoinEvent
    data object OnBack : JoinEvent
}

sealed interface JoinEffect : BaseUiEffect {
    data object StartOnboard : JoinEffect
    data class StartTermsWeb(val term: Term) : JoinEffect
}

enum class JoinRoute {
    NICKNAME,
    TERMS
}