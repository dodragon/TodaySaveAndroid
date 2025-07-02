package com.dojagy.todaysave.data.dto.user

data class JoinRequestDto(
    val email: String,
    val sns: String,
    val snsKey: String,
    val nickname: String
)