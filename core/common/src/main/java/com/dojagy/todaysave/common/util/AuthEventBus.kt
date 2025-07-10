package com.dojagy.todaysave.common.util

import kotlinx.coroutines.flow.Flow

interface AuthEventBus {

    val events: Flow<AuthEvent>

    suspend fun postEvent(event: AuthEvent)
}