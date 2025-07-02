package com.dojagy.todaysave.data.model

import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.dto.user.TokenDto
import com.dojagy.todaysave.data.dto.user.UserDto

fun UserDto?.mapper(
    snsType: SnsType,
    snsKey: String
) = UserModel(
    id = this?.id ?: Long.DEFAULT,
    nickname = this?.nickname ?: String.DEFAULT,
    email = this?.email ?: String.DEFAULT,
    sns = snsType,
    snsKey = snsKey,
    birthday = this?.birthday,
    gender = try {
        Gender.valueOf((this?.gender ?: String.DEFAULT).uppercase())
    }catch (e: Exception){
        e.printStackTrace()
        null
    },
    grade = try {
        UserGrade.valueOf((this?.grade ?: String.DEFAULT).uppercase())
    }catch (e: Exception) {
        e.printStackTrace()
        UserGrade.DEFAULT
    }
)

fun UserDto?.mapper() = UserModel(
    id = this?.id ?: Long.DEFAULT,
    nickname = this?.nickname ?: String.DEFAULT,
    email = this?.email ?: String.DEFAULT,
    birthday = this?.birthday,
    gender = try {
        Gender.valueOf((this?.gender ?: String.DEFAULT).uppercase())
    }catch (e: Exception){
        e.printStackTrace()
        null
    },
    grade = try {
        UserGrade.valueOf((this?.grade ?: String.DEFAULT).uppercase())
    }catch (e: Exception) {
        e.printStackTrace()
        UserGrade.DEFAULT
    }
)

fun TokenDto?.mapper() = TokenModel(
    accessToken = this?.accessToken ?: String.DEFAULT,
    refreshToken = this?.refreshToken ?: String.DEFAULT
)