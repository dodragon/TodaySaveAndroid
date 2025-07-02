package com.dojagy.todaysave.data.domain.usecase

import com.dojagy.todaysave.data.domain.Return
import com.dojagy.todaysave.data.domain.repository.UserApiRepository
import com.dojagy.todaysave.data.domain.repository.UserDatastoreRepository
import com.dojagy.todaysave.data.domain.onSuccess
import com.dojagy.todaysave.data.domain.repository.TokenDatastoreRepository
import com.dojagy.todaysave.data.model.LoginResultModel
import com.dojagy.todaysave.data.model.SnsType
import com.dojagy.todaysave.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val apiRepo: UserApiRepository,
    private val userDatastoreRepo: UserDatastoreRepository,
    private val tokenDatastoreRepo: TokenDatastoreRepository
) : BaseUseCase() {

    suspend fun checkNickname(
        nickname: String
    ): Flow<Return<Unit>> {
        return apiRepo.checkNickname(nickname).continuousResult()
    }

    //유저 정보를 수정하는 경우 api response로 데이터 받아서 set만 해주면
    //해당 Flow에서 알아서 바뀌어서 UI에 뿌려질거임
    fun myInfo(): Flow<Return<UserModel>> {
        return userDatastoreRepo.user.map {
            Return.success(it)
        }
    }

    suspend fun join(
        email: String,
        snsType: SnsType,
        snsKey: String,
        nickname: String
    ): Return<LoginResultModel> {
        return apiRepo.join(
            email = email,
            snsType = snsType,
            snsKey = snsKey,
            nickname = nickname
        ).oneTimeResult(true).onSuccess { model ->
            userLoginProcess(model)
        }
    }

    suspend fun login(
        email: String,
        snsKey: String,
        snsType: SnsType
    ): Return<LoginResultModel> {
        return apiRepo.login(
            email = email,
            snsKey = snsKey,
            snsType = snsType
        ).oneTimeResult(true).onSuccess { model ->
            userLoginProcess(model)
        }
    }

    private suspend fun userLoginProcess(
        model: LoginResultModel
    ) {
        userDatastoreRepo.setUser(model.user)
        tokenDatastoreRepo.setTokens(
            accessToken = model.token.accessToken,
            refreshToken = model.token.refreshToken
        )
    }
}