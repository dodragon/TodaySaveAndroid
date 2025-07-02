package com.dojagy.todaysave.data.source.datastore.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingPreferences {

    private const val APP_ONBOARD_ENABLED_KEY = "APP_ONBOARD_ENABLED"
    private const val USABLE_ONBOARD_ENABLED_KEY = "USABLE_ONBOARD_ENABLED"
    private const val LAST_SNS_TYPE_KEY = "LAST_SNS_TYPE"

    val APP_ONBOARD_ENABLED = booleanPreferencesKey(APP_ONBOARD_ENABLED_KEY)
    val USABLE_ONBOARD_ENABLED = booleanPreferencesKey(USABLE_ONBOARD_ENABLED_KEY)
    val LAST_SNS_TYPE = stringPreferencesKey(LAST_SNS_TYPE_KEY)
}