package com.dojagy.todaysave.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dojagy.todaysave.data.source.datastore.qulifier.SettingDatastore
import com.dojagy.todaysave.data.source.datastore.qulifier.TokenDatastore
import com.dojagy.todaysave.data.source.datastore.qulifier.UserDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    private const val SETTING_PREFERENCE = "TS_SETTING_PF"
    private const val TOKEN_PREFERENCE = "TS_TOKEN_PF"
    private const val USER_PREFERENCE = "TS_USER_PF"

    private val Context.settingDatastore: DataStore<Preferences> by preferencesDataStore(name = SETTING_PREFERENCE)
    private val Context.tokenDatastore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_PREFERENCE)
    private val Context.userDatastore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCE)

    @Provides
    @Singleton
    @SettingDatastore
    fun provideSettingPreference(
        @ApplicationContext
        context: Context
    ) = context.settingDatastore

    @Provides
    @Singleton
    @TokenDatastore
    fun provideTokenPreference(
        @ApplicationContext
        context: Context
    ) = context.tokenDatastore

    @Provides
    @Singleton
    @UserDatastore
    fun provideUserPreference(
        @ApplicationContext
        context: Context
    ) = context.userDatastore
}