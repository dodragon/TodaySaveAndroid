package com.dojagy.todaysave.data.source.datastore.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.domain.repository.SettingDatastoreRepository
import com.dojagy.todaysave.data.source.datastore.preferences.SettingPreferences
import com.dojagy.todaysave.data.source.datastore.qulifier.SettingDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingDatastoreRepositoryImpl @Inject constructor(
    @SettingDatastore
    private val datastore: DataStore<Preferences>
) : SettingDatastoreRepository {

    //앱 첫 온보딩
    override suspend fun setAppOnboardFalse() {
        datastore.edit { settings ->
            settings[SettingPreferences.APP_ONBOARD_ENABLED] = false
        }
    }

    //값이 true 인 경우에 온보딩 활성화
    override val isAppOnboardEnabled: Flow<Boolean> = datastore.data
        .map { preferences ->
            preferences[SettingPreferences.APP_ONBOARD_ENABLED] != false
        }

    //사용 방법에 대한 온보딩
    override suspend fun setUsableOnboardFalse() {
        datastore.edit { settings ->
            settings[SettingPreferences.USABLE_ONBOARD_ENABLED] = false
        }
    }

    //값이 true 인 경우에 온보딩 활성화
    override val isUsableOnboardEnabled: Flow<Boolean> = datastore.data
        .map { preferences ->
            preferences[SettingPreferences.USABLE_ONBOARD_ENABLED] != false
        }

    //마지막 Sns Type
    override suspend fun setLastSnsType(
        type: String
    ) {
        datastore.edit { settings ->
            settings[SettingPreferences.LAST_SNS_TYPE] = type.uppercase()
        }
    }

    //마지막 Sns Type
    override val lastSnsType: Flow<String> = datastore.data
        .map { preferences ->
            preferences[SettingPreferences.LAST_SNS_TYPE] ?: String.DEFAULT
        }
}