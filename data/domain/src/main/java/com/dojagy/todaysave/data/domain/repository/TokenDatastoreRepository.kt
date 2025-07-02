package com.dojagy.todaysave.data.domain.repository

import com.dojagy.todaysave.data.model.user.TokenModel
import kotlinx.coroutines.flow.Flow

interface TokenDatastoreRepository {
    val currentToken: TokenModel?
    val token: Flow<TokenModel>
    suspend fun setTokens(
        accessToken: String,
        refreshToken: String
    )
    suspend fun clearTokens()
}