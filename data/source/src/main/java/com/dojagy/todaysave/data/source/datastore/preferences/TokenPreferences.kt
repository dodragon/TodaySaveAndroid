package com.dojagy.todaysave.data.source.datastore.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenPreferences {
    private const val ACCESS_TOKEN_KEY = "TS_ACCESS_TOKEN"
    private const val REFRESH_TOKEN_KEY = "TS_REFRESH_TOKEN"

    val ACCESS_TOKEN = stringPreferencesKey(ACCESS_TOKEN_KEY)
    val REFRESH_TOKEN = stringPreferencesKey(REFRESH_TOKEN_KEY)
}