package com.dojagy.todaysave.data.model.user

import com.dojagy.todaysave.common.extension.DEFAULT

data class TokenModel(
    val accessToken: String = String.DEFAULT,
    val refreshToken: String = String.DEFAULT
)
