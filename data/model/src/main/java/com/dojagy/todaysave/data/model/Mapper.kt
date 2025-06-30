package com.dojagy.todaysave.data.model

import com.dojagy.todaysave.data.dto.MainDto

fun <E, M> MainDto<E?>.mainMapper(
    modelMapper: (E?) -> M? = { _ -> null }
): MainModel<M?> {
    return MainModel(
        state = when(this.status) {
            "success" -> ApiState.SUCCESS
            "fail" -> ApiState.FAIL
            else -> ApiState.ERROR
        },
        message = this.message ?: "오류가 발생했습니다.",
        data = modelMapper(this.data)
    )
}