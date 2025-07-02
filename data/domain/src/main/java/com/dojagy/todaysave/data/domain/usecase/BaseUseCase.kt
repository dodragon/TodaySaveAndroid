package com.dojagy.todaysave.data.domain.usecase

import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.data.domain.Return
import com.dojagy.todaysave.data.model.ApiState
import com.dojagy.todaysave.data.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class BaseUseCase {

    suspend fun <T> Flow<MainModel<T>>.oneTimeResult(
        isNeedData: Boolean = false
    ): Return<T> {
        val resultModel = this.first()
        return modelStateDiv(resultModel, isNeedData)
    }

    fun <T> Flow<MainModel<T>>.continuousResult(
        isNeedData: Boolean = false
    ): Flow<Return<T>> {
        return this.map { resultModel ->
            modelStateDiv(resultModel, isNeedData)
        }
    }

    private fun <T> modelStateDiv(
        model: MainModel<T>,
        isNeedData: Boolean = false
    ): Return<T> {
        return when(model.state) {
            ApiState.SUCCESS -> {
                val resultData = model.data
                if (resultData != null) {
                    Return.Companion.success(resultData)
                } else {
                    if(isNeedData.isTrue()) {
                        Return.Companion.failure(model.message ?: "데이터가 없습니다.")
                    }else {
                        Return.Companion.success()
                    }
                }
            }
            ApiState.DEFAULT -> {
                Return.Companion.failure(model.message ?: "응답을 받지 못했습니다.")
            }
            else -> {
                Return.Companion.failure(model.message ?: "오류가 발생했습니다.")
            }
        }
    }
}