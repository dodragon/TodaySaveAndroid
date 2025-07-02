package com.dojagy.todaysave.data.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingDatastoreRepository {
    val isAppOnboardEnabled: Flow<Boolean>
    val isUsableOnboardEnabled: Flow<Boolean>
    val lastSnsType: Flow<String>
    suspend fun setAppOnboardFalse()
    suspend fun setUsableOnboardFalse()
    suspend fun setLastSnsType(
        type: String
    )
}