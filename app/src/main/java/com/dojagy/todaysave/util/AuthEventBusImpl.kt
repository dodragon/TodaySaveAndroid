package com.dojagy.todaysave.util

import com.dojagy.todaysave.common.util.AuthEvent
import com.dojagy.todaysave.common.util.AuthEventBus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventBusImpl @Inject constructor() : AuthEventBus {

    private val _events = MutableSharedFlow<AuthEvent>()
    override val events = _events.asSharedFlow()

    override suspend fun postEvent(event: AuthEvent) {
        _events.emit(event)
    }
}