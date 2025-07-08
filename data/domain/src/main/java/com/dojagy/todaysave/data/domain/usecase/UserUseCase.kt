package com.dojagy.todaysave.data.domain.usecase

import com.dojagy.todaysave.common.extension.isFalse
import com.dojagy.todaysave.data.domain.Return
import com.dojagy.todaysave.data.domain.repository.UserApiRepository
import com.dojagy.todaysave.data.domain.repository.UserDatastoreRepository
import com.dojagy.todaysave.data.domain.onSuccess
import com.dojagy.todaysave.data.domain.repository.SettingDatastoreRepository
import com.dojagy.todaysave.data.domain.repository.TokenDatastoreRepository
import com.dojagy.todaysave.data.model.user.LoginResultModel
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.model.user.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val apiRepo: UserApiRepository,
    private val userDatastoreRepo: UserDatastoreRepository,
    private val tokenDatastoreRepo: TokenDatastoreRepository,
    private val settingDatastoreRepo: SettingDatastoreRepository
) : BaseUseCase() {

    val isLogin: Flow<Boolean> = userDatastoreRepo.user.map {
        it.snsKey.isBlank().isFalse()
    }

    suspend fun checkNickname(
        nickname: String
    ): Return<Unit> {
        return apiRepo.checkNickname(nickname).oneTimeResult()
    }

    suspend fun randomNickname(): Return<String> {
        return apiRepo.randomNickname().oneTimeResult()
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

    suspend fun logout(): Return<Unit> {
        return try {
            tokenDatastoreRepo.clearTokens()
            userDatastoreRepo.clearUser()
            Return.success()
        }catch (e: Exception) {
            e.printStackTrace()
            Return.failure("로그아웃에 실패했습니다.")
        }
    }

    suspend fun lastLoginType(): Return<SnsType> {
        return settingDatastoreRepo.lastSnsType.map {
            try {
                Return.success(SnsType.valueOf(it))
            }catch (e: Exception) {
                e.printStackTrace()
                Return.success()
            }
        }.first()
    }

    suspend fun setLastLogin(
        snsType: SnsType
    ) {
        settingDatastoreRepo.setLastSnsType(snsType.strValue)
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