package com.dojagy.todaysave.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dojagy.todaysave.common.util.AuthEvent
import com.dojagy.todaysave.common.util.AuthEventBus
import com.dojagy.todaysave.data.view.AuthEventHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HiltAuthEventHandler @Inject constructor(
    private val authEventBus: AuthEventBus
) : AuthEventHandler {
    override fun observeAuthEvents(
        owner: LifecycleOwner,
        onLogout: () -> Unit
    ) {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authEventBus.events.collectLatest { event ->
                    if (event == AuthEvent.LOGOUT_SUCCESS) {
                        onLogout()
                    }
                }
            }
        }
    }
}