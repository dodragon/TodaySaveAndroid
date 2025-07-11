package com.dojagy.todaysave.common.util

class Return<T> private constructor(
    var status: Int,
    val value: T?,
    val message: String? = null
) {

    companion object {
        const val SUCCESS: Int = 0
        const val FAILURE: Int = 1
        const val INVALID_TOKEN: Int = 2

        fun<T> success(): Return<T> {
            return Return(status = SUCCESS, value = null, message = null)
        }

        fun<T> success(value: T): Return<T> {
            return Return(status = SUCCESS, value = value)
        }

        fun<T> success(value: T, message: String): Return<T> {
            return Return(status = SUCCESS, value = value, message = message)
        }

        fun<T> failure(): Return<T> {
            return Return(status = FAILURE, value = null, message = null)
        }

        fun<T> failure(message: String): Return<T> {
            return Return(status = FAILURE, value = null, message = message)
        }

        fun<T> invalidToken(): Return<T> {
            return Return(status = INVALID_TOKEN, value = null, message = null)
        }
    }
}

inline fun <reified T> Return<T>.onSuccess(
    action: (value: T) -> Unit
): Return<T> {
    if (status == Return.SUCCESS && value == null) {
        action(Unit as T)
    } else if (status == Return.SUCCESS && value != null) {
        action(value)
    }
    return this
}

inline fun <reified T> Return<T>.onFailure(
    action: (message: String) -> Unit
): Return<T> {
    if (status == Return.FAILURE) {
        action(message ?: "오류가 발생했습니다.")
    }
    return this
}

inline fun <T> Return<T>.onInvalidToken(
    action: () -> Unit
): Return<T> {
    if(status == Return.INVALID_TOKEN) {
        action()
    }
    return this
}