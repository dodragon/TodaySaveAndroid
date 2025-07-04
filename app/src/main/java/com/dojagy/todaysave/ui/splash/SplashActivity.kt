package com.dojagy.todaysave.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.extension.goMarket
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.ui.auth.AuthActivity
import com.dojagy.todaysave.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashState, SplashEffect, SplashEvent, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        DLog.e("UI_STATE", uiState)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "SPLASH",
                fontSize = 34.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        if (uiState.updateRequired) {
            //TODO: 업데이트 dialog show
        }
    }

    override fun activityHandleEffect(effect: BaseUiEffect) {
        super.activityHandleEffect(effect)
        DLog.e("UI_EFFECT", effect)
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
        }
    }

    override fun onBack() {}
}