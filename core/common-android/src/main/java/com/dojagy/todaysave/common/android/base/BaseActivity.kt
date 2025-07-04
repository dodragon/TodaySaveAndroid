package com.dojagy.todaysave.common.android.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dojagy.todaysave.data.view.snackbar.CustomSnackBarHost
import com.dojagy.todaysave.data.view.theme.TodaySaveTheme
import kotlinx.coroutines.launch

abstract class BaseActivity<S : BaseUiState, E : BaseUiEffect, V : BaseUiEvent, VM : BaseViewModel<S, E, V>> :
    ComponentActivity() {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEffect.collect { effect ->
                    defaultHandleEffect(effect)
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var snackBarMessages by remember { mutableStateOf<List<SnackBarMessage<V>>>(emptyList()) }

            LaunchedEffect(Unit) {
                viewModel.uiEffect.collect { effect ->
                    if(effect is BaseUiEffect.ShowSnackBar<*>) {
                        @Suppress("UNCHECKED_CAST")
                        snackBarMessages = snackBarMessages + (effect.snackBarData as SnackBarMessage<V>)
                    }else if (effect is BaseUiEffect.ShowToast) {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }else {
                        defaultHandleEffect(effect)
                    }
                }
            }

            TodaySaveTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(color = Color.White)
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
                            Content(
                                modifier = Modifier
                                    .matchParentSize()
                            )

                            CustomSnackBarHost(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .imePadding(),
                                messages = snackBarMessages,
                                onDismiss = { messageId ->
                                    snackBarMessages = snackBarMessages.filterNot { it.id == messageId }
                                },
                                onAction = { messageId, event ->
                                    snackBarMessages = snackBarMessages.filterNot { it.id == messageId }
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
        notComposeHandleEffect(effect)
    }

    //Composable 내에서 실행 하지 않아야 되는 effect
    protected open fun notComposeHandleEffect(effect: BaseUiEffect) {}

    @Composable
    abstract fun TopView(
        modifier: Modifier = Modifier
    )

    @Composable
    abstract fun Content(
        modifier: Modifier = Modifier
    )

    @Composable
    abstract fun BottomView(
        modifier: Modifier = Modifier
    )

    abstract fun onBack()
}