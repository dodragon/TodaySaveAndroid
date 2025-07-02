package com.dojagy.todaysave.data.dto.user

data class TokenReissueRequestDto(
    val accessToken: String,
    val refreshToken: String
)
