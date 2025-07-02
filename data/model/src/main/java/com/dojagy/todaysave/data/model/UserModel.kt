package com.dojagy.todaysave.data.model

import com.dojagy.todaysave.common.extension.DEFAULT
import java.time.LocalDate

data class UserModel(
    val id: Long = Long.DEFAULT,
    val nickname: String = String.DEFAULT,
    val email: String = String.DEFAULT,
    val sns: SnsType = SnsType.DEFAULT,
    val snsKey: String = String.DEFAULT,
    val gender: Gender? = null,
    val birthday: LocalDate? = null,
    val grade: UserGrade = UserGrade.DEFAULT
)

enum class SnsType(
    val strValue: String
) {
    DEFAULT(""),
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE")
}

enum class Gender(
    val strValue: String
) {
    MALE("MALE"),
    FEMALE("FEMALE")
}

enum class UserGrade(
    val strValue: String
) {
    DEFAULT("DEFAULT"),
    PREMIUM("PREMIUM")
}