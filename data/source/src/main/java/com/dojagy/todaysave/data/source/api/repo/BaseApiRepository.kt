package com.dojagy.todaysave.data.source.api.repo

import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.data.dto.MainDto
import com.dojagy.todaysave.data.model.ApiState
import com.dojagy.todaysave.data.model.MainModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.net.SocketException

open class BaseApiRepository {

    protected suspend fun <E, M> responseToModel(
        response: suspend () -> Response<MainDto<E>>,
        mapper: ((E) -> M)? = null,
    ): Flow<MainModel<M>> {
        val maxRetries = 3
        var currentDelay = 1000L

        for (attempt in 1..maxRetries) {
            try {
                // 1. 네트워크 요청 및 기본 응답 처리
                val responseData = response.invoke()
                val apiState = setApiStatus(responseData)
                val message = setMessage(apiState, responseData.body()?.message)
                val data = responseData.body()?.data

                return flow {
                    emit(
                        MainModel<M>(
                            state = apiState,
                            message = message,
                            data = if (data != null) {
                                mapper?.invoke(data)
                            } else {
                                null
                            }
                        )
                    )
                }
            } catch (e: SocketException) {
                // Software caused connection abort 와 같은 오류를 여기서 잡음
                DLog.e(
                    "NetworkRetry",
                    "Attempt $attempt/$maxRetries failed with SocketException: ${e.message}"
                )
                e.printStackTrace()
                if (attempt < maxRetries) {
                    DLog.i("NetworkRetry", "Retrying in $currentDelay ms...")
                    delay(currentDelay) // 코루틴 delay를 사용하여 논블로킹 대기
                    currentDelay *= 2 // 다음 대기 시간을 2배로 늘림 (지수 백오프)
                }
            } catch (e: IOException) {
                // 그 외 다른 네트워크 I/O 오류 (예: 연결 시간 초과)
                DLog.e(
                    "NetworkRetry",
                    "Attempt $attempt/$maxRetries failed with IOException: ${e.message}"
                )
                e.printStackTrace()
                if (attempt < maxRetries) {
                    DLog.i("NetworkRetry", "Retrying in $currentDelay ms...")
                    delay(currentDelay)
                    currentDelay *= 2
                }
            }
        }

        return flow {
            emit(
                MainModel(
                    state = ApiState.ERROR,
                    message = "네트워크 상태를 확인해주세요.",
                    data = null
                )
            )
        }
    }

    private fun <E> setApiStatus(
        responseData: Response<MainDto<E>>
    ): ApiState {
        return when(responseData.code()) {
            200 -> {
                when(responseData.body()?.status) {
                    "success" -> ApiState.SUCCESS
                    "fail" -> ApiState.FAIL
                    else -> ApiState.ERROR
                }
            }
            401 -> ApiState.INVALID_TOKEN
            else -> ApiState.ERROR
        }
    }

    private fun setMessage(
        state: ApiState,
        message: String?
    ): String {
        return when(state) {
            ApiState.DEFAULT -> String.Companion.DEFAULT
            ApiState.SUCCESS -> message ?: String.DEFAULT
            ApiState.FAIL -> message ?: "오류가 발생했습니다."
            ApiState.ERROR -> message ?: "오류가 발생했습니다."
            ApiState.INVALID_TOKEN -> "회원 인증 정보가 만료되었습니다."
        }
    }
}