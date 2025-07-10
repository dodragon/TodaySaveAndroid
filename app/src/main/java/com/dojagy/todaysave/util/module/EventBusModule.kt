package com.dojagy.todaysave.util.module

import com.dojagy.todaysave.common.util.AuthEventBus
import com.dojagy.todaysave.util.AuthEventBusImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventBusModule {

    @Binds
    @Singleton
    abstract fun bindAuthEventBus(
        authEventBusImpl: AuthEventBusImpl
    ): AuthEventBus
}