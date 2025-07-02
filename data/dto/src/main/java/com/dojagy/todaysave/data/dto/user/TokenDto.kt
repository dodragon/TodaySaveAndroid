package com.dojagy.todaysave.data.dto.user

data class TokenDto(
    val grantType: String?,
    val accessToken: String?,
    val refreshToken: String?
)
