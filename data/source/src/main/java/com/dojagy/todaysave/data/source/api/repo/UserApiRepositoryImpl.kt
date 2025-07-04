package com.dojagy.todaysave.data.source.api.repo

import com.dojagy.todaysave.data.domain.repository.UserApiRepository
import com.dojagy.todaysave.data.dto.user.JoinRequestDto
import com.dojagy.todaysave.data.dto.user.LoginRequestDto
import com.dojagy.todaysave.data.model.user.LoginResultModel
import com.dojagy.todaysave.data.model.MainModel
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.model.user.UserModel
import com.dojagy.todaysave.data.model.mapper
import com.dojagy.todaysave.data.source.api.service.UserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserApiRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserApiRepository, BaseApiRepository() {

    override suspend fun join(
        email: String,
        snsType: SnsType,
        snsKey: String,
        nickname: String
    ): Flow<MainModel<LoginResultModel>> {
        return responseToModel(
            response = {
                userService.join(JoinRequestDto(
                    email = email,
                    sns = snsType.strValue,
                    snsKey = snsKey,
                    nickname = nickname
                ))
            },
            mapper = { dto ->
                LoginResultModel(
                    user = dto.userInfo.mapper(
                        snsType = snsType,
                        snsKey = snsKey
                    ),
                    token = dto.tokenInfo.mapper()
                )
            }
        )
    }

    override suspend fun login(
        email: String,
        snsKey: String,
        snsType: SnsType
    ): Flow<MainModel<LoginResultModel>> {
        return responseToModel(
            response = {
                userService.login(LoginRequestDto(
                    email = email,
                    snsKey = snsKey
                ))
            },
            mapper = { dto ->
                LoginResultModel(
                    user = dto.userInfo.mapper(
                        snsType = snsType,
                        snsKey = snsKey
                    ),
                    token = dto.tokenInfo.mapper()
                )
            }
        )
    }

    override suspend fun checkNickname(
        nickname: String
    ): Flow<MainModel<Unit>> {
        return responseToModel(
            response = {
                userService.checkNickname(nickname)
            }
        )
    }

    override suspend fun myInfo(): Flow<MainModel<UserModel>> {
        return responseToModel(
            response = {
                userService.myInfo()
            },
            mapper = {
                it.mapper()
            }
        )
    }
}