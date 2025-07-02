package com.dojagy.todaysave.data.source.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dojagy.todaysave.data.domain.repository.SettingDatastoreRepository
import com.dojagy.todaysave.data.domain.repository.TokenDatastoreRepository
import com.dojagy.todaysave.data.domain.repository.UserApiRepository
import com.dojagy.todaysave.data.domain.repository.UserDatastoreRepository
import com.dojagy.todaysave.data.source.api.repo.UserApiRepositoryImpl
import com.dojagy.todaysave.data.source.api.service.UserService
import com.dojagy.todaysave.data.source.datastore.qulifier.SettingDatastore
import com.dojagy.todaysave.data.source.datastore.qulifier.TokenDatastore
import com.dojagy.todaysave.data.source.datastore.qulifier.UserDatastore
import com.dojagy.todaysave.data.source.datastore.repo.SettingDatastoreRepositoryImpl
import com.dojagy.todaysave.data.source.datastore.repo.TokenDatastoreRepositoryImpl
import com.dojagy.todaysave.data.source.datastore.repo.UserDatastoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /** API **/

    @Provides
    @Singleton
    fun provideUserApiRepo(
        api: UserService
    ): UserApiRepository = UserApiRepositoryImpl(api)


    /** Datastore **/

    @Provides
    @Singleton
    fun provideUserDatastoreRepo(
        @UserDatastore
        datastore: DataStore<Preferences>
    ): UserDatastoreRepository = UserDatastoreRepositoryImpl(datastore)

    @Provides
    @Singleton
    fun provideTokenDatastoreRepo(
        @TokenDatastore
        datastore: DataStore<Preferences>
    ): TokenDatastoreRepository = TokenDatastoreRepositoryImpl(datastore)

    @Provides
    @Singleton
    fun provideSettingDatastoreRepo(
        @SettingDatastore
        datastore: DataStore<Preferences>
    ): SettingDatastoreRepository = SettingDatastoreRepositoryImpl(datastore)
}