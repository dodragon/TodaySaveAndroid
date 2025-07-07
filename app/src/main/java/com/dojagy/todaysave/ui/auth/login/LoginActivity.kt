package com.dojagy.todaysave.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dojagy.todaysave.R
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.sns.GoogleLogin
import com.dojagy.todaysave.common.android.sns.KakaoLogin
import com.dojagy.todaysave.common.android.sns.NaverLogin
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.theme.TodaySaveTheme
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.ui.auth.join.JoinActivity
import com.dojagy.todaysave.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginState, LoginEffect, LoginEvent, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    @Composable
    override fun Content() {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                modifier = Modifier.Companion
                    .width(100.dp)
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.Black
                ),
                onClick = {
                    viewModel.handleEvent(LoginEvent.Login(SnsType.GOOGLE))
                }
            ) {
                Text(
                    text = "구글",
                    fontSize = 14.sp,
                    color = Color.Companion.White
                )
            }

            Button(
                modifier = Modifier.Companion
                    .width(100.dp)
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.Black
                ),
                onClick = {
                    viewModel.handleEvent(LoginEvent.Login(SnsType.KAKAO))
                }
            ) {
                Text(
                    text = "카카오",
                    fontSize = 14.sp,
                    color = Color.Companion.White
                )
            }

            Button(
                modifier = Modifier.Companion
                    .width(100.dp)
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.Black
                ),
                onClick = {
                    viewModel.handleEvent(LoginEvent.Login(SnsType.NAVER))
                }
            ) {
                Text(
                    text = "네이버",
                    fontSize = 14.sp,
                    color = Color.Companion.White
                )
            }
        }
    }

    override fun activityHandleEffect(effect: BaseUiEffect) {
        super.activityHandleEffect(effect)
        when (effect) {
            is LoginEffect.LoginStart -> {
                snsLogin(effect.type)
            }
            is LoginEffect.LoginSuccess -> {
                viewModel.setLastLogin(effect.type)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            is LoginEffect.LoginFail -> {
                viewModel.showErrorSnackBar(effect.msg)
                if (effect.joinData != null) {
                    val intent = Intent(this@LoginActivity, JoinActivity::class.java)
                    intent.putExtra("email", effect.joinData.email)
                    intent.putExtra("snsType", effect.joinData.snsType)
                    intent.putExtra("snsKey", effect.joinData.snsKey)
                    intent.putExtra("nickname", effect.joinData.nickname)
                    startActivity(intent)
                }
            }
        }
    }

    private fun snsLogin(
        snsType: SnsType
    ) {
        when (snsType) {
            SnsType.KAKAO -> {
                KakaoLogin(
                    context = this,
                    onComplete = this@LoginActivity::login,
                    onError = viewModel::loginError
                ).login()
            }
            SnsType.NAVER -> {
                NaverLogin(
                    context = this,
                    clientId = getString(R.string.naver_client_id),
                    clientSecret = getString(R.string.naver_client_secret),
                    clientName = getString(R.string.naver_client_name),
                    onComplete = this@LoginActivity::login,
                    onError = viewModel::loginError
                ).login()
            }
            SnsType.GOOGLE -> {
                lifecycleScope.launch {
                    GoogleLogin(
                        context = this@LoginActivity,
                        webKey = getString(R.string.default_web_client_id),
                        onComplete = this@LoginActivity::login,
                        onError = viewModel::loginError
                    ).login()
                }
            }
            else -> {}
        }
    }

    private fun login(
        snsKey: String,
        snsType: String,
        email: String,
        nickname: String?,
    ) {
        viewModel.requestLogin(
            email = email,
            snsKey = snsKey,
            snsType = SnsType.valueOf(snsType),
            nickname = nickname ?: String.DEFAULT
        )
    }

    override fun onBack() {
        finish()
    }
}