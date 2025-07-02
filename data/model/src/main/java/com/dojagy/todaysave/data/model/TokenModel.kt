package com.dojagy.todaysave.data.model

import com.dojagy.todaysave.common.extension.DEFAULT

data class TokenModel(
    val accessToken: String = String.DEFAULT,
    val refreshToken: String = String.DEFAULT
)
