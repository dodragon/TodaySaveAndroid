package com.dojagy.todaysave.data.view

import androidx.lifecycle.LifecycleOwner

interface AuthEventHandler {

    fun observeAuthEvents(owner: LifecycleOwner, onLogout: () -> Unit)
}