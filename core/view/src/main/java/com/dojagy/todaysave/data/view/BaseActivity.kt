package com.dojagy.todaysave.data.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.android.base.SnackBarMessage
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.theme.TodaySaveTheme
import com.dojagy.todaysave.data.view.snackbar.CustomSnackBarHost
import kotlinx.coroutines.launch

abstract class BaseActivity<S : BaseUiState, E : BaseUiEffect, V : BaseUiEvent, VM : BaseViewModel<S, E, V>> :
    ComponentActivity() {

    protected abstract val viewModel: VM

    protected var useInnerPadding = true

    private var snackBarMessages by mutableStateOf<List<SnackBarMessage<V>>>(emptyList())

    abstract val authEventHandler: AuthEventHandler
    abstract val loginActivityClassName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authEventHandler.observeAuthEvents(this) {
            handleLogout()
        }

        enableEdgeToEdge()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEffect.collect {effect ->
                    if (effect is BaseUiEffect.ShowSnackBar<*>) {
                        @Suppress("UNCHECKED_CAST")
                        snackBarMessages =
                            snackBarMessages + (effect.snackBarData as SnackBarMessage<V>)
                    } else if (effect is BaseUiEffect.ShowToast) {
                        Toast.makeText(this@BaseActivity, effect.message, Toast.LENGTH_SHORT).show()
                    } else {
                        defaultHandleEffect(effect)
                    }
                }
            }
        }

        setContent {
            TodaySaveTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(if(useInnerPadding) innerPadding else PaddingValues(Int.DEFAULT.dp))
                    ) {
                        BackHandler {
                            onBack()
                        }

                        TopView(
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(color = Color.White)
                        ) {
                            Content()

                            CustomSnackBarHost<V>(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .imePadding(),
                                messages = snackBarMessages,
                                onDismiss = { messageId ->
                                    snackBarMessages =
                                        snackBarMessages.filterNot { it.id == messageId }
                                },
                                onAction = { messageId, event ->
                                    snackBarMessages =
                                        snackBarMessages.filterNot { it.id == messageId }
                                    viewModel.handleEvent(event)
                                }
                            )
                        }

                        BottomView(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    private fun defaultHandleEffect(
        effect: BaseUiEffect
    ) {
        activityHandleEffect(effect)
    }

    protected open fun activityHandleEffect(effect: BaseUiEffect) {}

    @Composable
    open fun TopView(
        modifier: Modifier
    ) {}

    @Composable
    abstract fun Content()

    @Composable
    open fun BottomView(
        modifier: Modifier
    ){}

    @Composable
    protected fun StatusBarLightIcon() {
        val view = LocalView.current

        DisposableEffect(Unit) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.isAppearanceLightStatusBars = false

            onDispose {
                insetsController.isAppearanceLightStatusBars = true
            }
        }
    }

    abstract fun onBack()

    private fun handleLogout() {
        if (this::class.java.name == loginActivityClassName) return

        val intent = Intent().setClassName(this, loginActivityClassName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}