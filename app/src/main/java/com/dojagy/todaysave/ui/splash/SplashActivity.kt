package com.dojagy.todaysave.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.extension.goMarket
import com.dojagy.todaysave.ui.auth.login.LoginActivity
import com.dojagy.todaysave.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashState, SplashEffect, SplashEvent, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        useInnerPadding = false
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun Content() {
        StatusBarLightIcon()

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
    }

    override fun activityHandleEffect(effect: BaseUiEffect) {
        super.activityHandleEffect(effect)
        when(effect) {
            is SplashEffect.NavigateLogin -> {
                startActivity(Intent(this, LoginActivity::class.java))
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