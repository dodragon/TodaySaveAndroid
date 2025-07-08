package com.dojagy.todaysave.data.domain.repository

import com.dojagy.todaysave.data.model.user.LoginResultModel
import com.dojagy.todaysave.data.model.MainModel
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.model.user.UserModel
import kotlinx.coroutines.flow.Flow

interface UserApiRepository {
    suspend fun join(
        email: String,
        snsType: SnsType,
        snsKey: String,
        nickname: String
    ): Flow<MainModel<LoginResultModel>>

    suspend fun login(
        email: String,
        snsKey: String,
        snsType: SnsType
    ): Flow<MainModel<LoginResultModel>>

    suspend fun checkNickname(
        nickname: String
    ): Flow<MainModel<Unit>>

    suspend fun randomNickname(): Flow<MainModel<String>>

    suspend fun myInfo(): Flow<MainModel<UserModel>>
}