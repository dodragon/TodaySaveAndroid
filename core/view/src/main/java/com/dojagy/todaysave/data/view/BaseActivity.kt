package com.dojagy.todaysave.data.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.BaseUiState
import com.dojagy.todaysave.common.android.base.BaseViewModel
import com.dojagy.todaysave.common.android.base.SnackBarMessage
import com.dojagy.todaysave.data.view.snackbar.CustomSnackBarHost
import com.dojagy.todaysave.core.resources.theme.TodaySaveTheme
import kotlinx.coroutines.launch
import kotlin.collections.plus

abstract class BaseActivity<S : BaseUiState, E : BaseUiEffect, V : BaseUiEvent, VM : BaseViewModel<S, E, V>> :
    ComponentActivity() {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var snackBarMessages by remember { mutableStateOf<List<SnackBarMessage<V>>>(emptyList()) }

            LaunchedEffect(Unit) {
                viewModel.uiEffect.collect { effect ->
                    if (effect is BaseUiEffect.ShowSnackBar<*>) {
                        @Suppress("UNCHECKED_CAST")
                        snackBarMessages =
                            snackBarMessages + (effect.snackBarData as SnackBarMessage<V>)
                    } else if (effect is BaseUiEffect.ShowToast) {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    } else {
                        defaultHandleEffect(effect)
                    }
                }
            }

            TodaySaveTheme {
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(color = Color.Companion.White)
                    ) {
                        BackHandler {
                            onBack()
                        }

                        TopView(
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                        )

                        Box(
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                                .weight(1f)
                                .background(color = Color.Companion.White)
                        ) {
                            Content()

                            CustomSnackBarHost<V>(
                                modifier = Modifier.Companion
                                    .align(Alignment.Companion.BottomCenter)
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
                            modifier = Modifier.Companion
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

    abstract fun onBack()
}