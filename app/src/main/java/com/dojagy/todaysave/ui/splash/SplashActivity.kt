package com.dojagy.todaysave.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.extension.goMarket
import com.dojagy.todaysave.ui.auth.AuthActivity
import com.dojagy.todaysave.ui.main.MainActivity
import kotlin.jvm.java

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) { // 또는 viewModel.uiEffect
                viewModel.uiEffect.collect { effect ->
                    handleSplashEffect(effect)
                }
            }

            if (uiState.updateRequired) {
                //TODO: 업데이트 dialog show
            }
        }
    }

    private fun handleSplashEffect(effect: BaseUiEffect) {
        when(effect) {
            is SplashEffect.NavigateLogin -> {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            is SplashEffect.NavigateMain -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            is SplashEffect.NavigateUpdate -> {
                goMarket(packageName)
            }
            is BaseUiEffect.ShowToast -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}