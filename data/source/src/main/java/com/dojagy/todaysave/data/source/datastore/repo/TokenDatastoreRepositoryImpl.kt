package com.dojagy.todaysave.data.source.datastore.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.domain.repository.TokenDatastoreRepository
import com.dojagy.todaysave.data.model.user.TokenModel
import com.dojagy.todaysave.data.source.datastore.preferences.TokenPreferences
import com.dojagy.todaysave.data.source.datastore.qulifier.TokenDatastore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDatastoreRepositoryImpl @Inject constructor(
    @TokenDatastore
    private val datastore: DataStore<Preferences>
) : TokenDatastoreRepository {

    private var cachedToken: TokenModel? = null
    override val currentToken: TokenModel?
        get() = cachedToken

    override val token: Flow<TokenModel> = datastore.data
        .map { preferences ->
            TokenModel(
                accessToken = preferences[TokenPreferences.ACCESS_TOKEN] ?: String.DEFAULT,
                refreshToken = preferences[TokenPreferences.REFRESH_TOKEN] ?: String.DEFAULT
            )
        }

    init {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            token.collect {
                cachedToken = it
            }
        }
    }

    override suspend fun setTokens(
        accessToken: String,
        refreshToken: String
    ) {
        datastore.edit { tokens ->
            tokens[TokenPreferences.ACCESS_TOKEN] = accessToken
            tokens[TokenPreferences.REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        datastore.edit { it.clear() }
    }
}