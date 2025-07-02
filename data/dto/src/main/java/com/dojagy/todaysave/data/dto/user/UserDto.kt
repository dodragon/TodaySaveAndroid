package com.dojagy.todaysave.data.dto.user

import java.time.LocalDate
import java.time.LocalDateTime

data class UserDto(
    val id: Long?,
    val email: String?,
    val nickname: String?,
    val birthday: LocalDate?,
    val gender: String?,
    val grade: String?,
    val createDt: LocalDateTime?
)
