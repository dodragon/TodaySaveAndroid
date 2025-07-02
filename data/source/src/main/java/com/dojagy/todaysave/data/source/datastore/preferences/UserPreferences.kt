package com.dojagy.todaysave.data.source.datastore.preferences

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferences {
    private const val USER_ID_KEY = "TS_USER_ID"
    private const val USER_EMAIL_KEY = "TS_USER_EMAIL"
    private const val USER_SNS_KEY_KEY = "TS_USER_SNS_KEY"
    private const val USER_SNS_TYPE_KEY = "TS_USER_SNS_TYPE"
    private const val USER_NICKNAME_KEY = "TS_USER_NICKNAME"
    private const val USER_BIRTHDAY_KEY = "TS_USER_BIRTHDAY"
    private const val USER_GENDER_KEY = "TS_USER_GENDER"
    private const val USER_GRADE_KEY = "TS_USER_GRADE"

    val USER_ID = longPreferencesKey(USER_ID_KEY)
    val USER_EMAIL = stringPreferencesKey(USER_EMAIL_KEY)
    val USER_SNS_KEY = stringPreferencesKey(USER_SNS_KEY_KEY)
    val USER_SNS_TYPE = stringPreferencesKey(USER_SNS_TYPE_KEY)
    val USER_NICKNAME = stringPreferencesKey(USER_NICKNAME_KEY)
    val USER_BIRTHDAY = stringPreferencesKey(USER_BIRTHDAY_KEY)
    val USER_GENDER = stringPreferencesKey(USER_GENDER_KEY)
    val USER_GRADE = stringPreferencesKey(USER_GRADE_KEY)
}