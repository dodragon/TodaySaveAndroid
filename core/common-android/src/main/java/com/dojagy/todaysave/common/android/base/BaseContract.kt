package com.dojagy.todaysave.common.android.base

import com.dojagy.todaysave.core.resources.wrapper.UiText
import java.util.UUID

/**
 * 모든 ViewModel의 UI 상태가 구현해야 하는 기본 인터페이스
 */
interface BaseUiState {
    val isLoading: Boolean
}

/**
 * 모든 ViewModel의 UI 효과가 구현해야 하는 기본 인터페이스
 */
interface BaseUiEffect {
    data class ShowToast(val message: String) : BaseUiEffect
    data class ShowSnackBar<out V : BaseUiEvent>(
        val snackBarData: SnackBarMessage<V>
    ) : BaseUiEffect
}

data class SnackBarMessage<out V : BaseUiEvent>(
    val id: String = UUID.randomUUID().toString(),
    val title: UiText? = null,
    val message: UiText,
    val type: SnackBarType = SnackBarType.Normal,
    val dismissType: DismissType<V>
)

sealed interface DismissType<out V : BaseUiEvent> {
    /** 2초 후 자동으로 사라짐 */
    data object Automatic : DismissType<Nothing>

    /** 닫기(X) 버튼으로 수동으로 닫아야 함 */
    data object Manual : DismissType<Nothing>

    /**
     * 액션 버튼을 가지며, 클릭 시 특정 이벤트를 ViewModel에 전달하고 사라짐.
     * @param actionLabel 버튼에 표시될 텍스트 (예: "실행 취소").
     * @param event ViewModel에 전달할 이벤트.
     */
    data class Action<out V : BaseUiEvent>(
        val actionLabel: String,
        val event: V
    ) : DismissType<V>
}

enum class SnackBarType {
    Normal, Success, Error
}

/**
 * 모든 ViewModel의 이벤트(사용자 액션)가 구현해야 하는 기본 인터페이스
 */
interface BaseUiEvent