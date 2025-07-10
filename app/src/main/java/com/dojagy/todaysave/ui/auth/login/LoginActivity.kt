package com.dojagy.todaysave.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.android.sns.GoogleLogin
import com.dojagy.todaysave.common.android.sns.KakaoLogin
import com.dojagy.todaysave.common.android.sns.NaverLogin
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.data.view.clickableSingle
import com.dojagy.todaysave.data.view.text.TopTooltip
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.ui.auth.join.JoinActivity
import com.dojagy.todaysave.ui.main.MainActivity
import com.dojagy.todaysave.util.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppBaseActivity<LoginState, LoginEffect, LoginEvent, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        useInnerPadding = false
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun Content() {
        StatusBarLightIcon()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(color = Gray4)
            ) {
                TsText(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "이미지 영역",
                    fontWeight = FontWeight.Bold,
                    size = 22.sp
                )
            }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val (helper, google, kakao, naver) = createRefs()

                DLog.e("state", uiState)

                if(uiState.lastLoginType != null) {
                    TopTooltip(
                        modifier = Modifier
                            .constrainAs(helper) {
                                when (uiState.lastLoginType) {
                                    SnsType.KAKAO -> {
                                        start.linkTo(anchor = kakao.start)
                                        end.linkTo(anchor = kakao.end)
                                        bottom.linkTo(anchor = kakao.top, margin = 8.dp)
                                    }

                                    SnsType.NAVER -> {
                                        start.linkTo(anchor = naver.start)
                                        end.linkTo(anchor = naver.end)
                                        bottom.linkTo(anchor = naver.top, margin = 8.dp)
                                    }

                                    SnsType.GOOGLE -> {
                                        start.linkTo(anchor = google.start)
                                        end.linkTo(anchor = google.end)
                                        bottom.linkTo(anchor = google.top, margin = 8.dp)
                                    }

                                    else -> {}
                                }
                            },
                        text = stringResource(R.string.last_login)
                    )
                }

                val iconSize = 48 / 360f

                Image(
                    modifier = Modifier
                        .constrainAs(google) {
                            start.linkTo(anchor = parent.start, margin = 80.dp)
                            bottom.linkTo(anchor = parent.bottom, margin = 40.dp)
                            width = Dimension.percent(iconSize)
                        }
                        .aspectRatio(1f)
                        .clickableSingle {
                            viewModel.handleEvent(LoginEvent.Login(SnsType.GOOGLE))
                        },
                    painter = painterResource(id = R.drawable.ic_google_login),
                    contentDescription = "google_login"
                )

                Image(
                    modifier = Modifier
                        .constrainAs(kakao) {
                            start.linkTo(anchor = google.end)
                            bottom.linkTo(anchor = google.bottom)
                            top.linkTo(anchor = google.top)
                            end.linkTo(anchor = naver.start)
                            width = Dimension.percent(iconSize)
                        }
                        .aspectRatio(1f)
                        .clickableSingle {
                            viewModel.handleEvent(LoginEvent.Login(SnsType.KAKAO))
                        },
                    painter = painterResource(id = R.drawable.ic_kakao_login),
                    contentDescription = "kakao_login"
                )

                Image(
                    modifier = Modifier
                        .constrainAs(naver) {
                            end.linkTo(anchor = parent.end, margin = 80.dp)
                            bottom.linkTo(anchor = parent.bottom, margin = 40.dp)
                            width = Dimension.percent(iconSize)
                        }
                        .aspectRatio(1f)
                        .clickableSingle {
                            viewModel.handleEvent(LoginEvent.Login(SnsType.NAVER))
                        },
                    painter = painterResource(id = R.drawable.ic_naver_login),
                    contentDescription = "naver_login"
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
                    clientId = getString(com.dojagy.todaysave.R.string.naver_client_id),
                    clientSecret = getString(com.dojagy.todaysave.R.string.naver_client_secret),
                    clientName = getString(com.dojagy.todaysave.R.string.naver_client_name),
                    onComplete = this@LoginActivity::login,
                    onError = viewModel::loginError
                ).login()
            }
            SnsType.GOOGLE -> {
                lifecycleScope.launch {
                    GoogleLogin(
                        context = this@LoginActivity,
                        webKey = getString(com.dojagy.todaysave.R.string.default_web_client_id),
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