package com.dojagy.todaysave.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.R
import com.dojagy.todaysave.common.android.sns.GoogleLogin
import com.dojagy.todaysave.common.android.sns.KakaoLogin
import com.dojagy.todaysave.common.android.sns.NaverLogin
import com.dojagy.todaysave.data.view.theme.TodaySaveTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            TodaySaveTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Cyan),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = {
                            scope.launch {
                                GoogleLogin(
                                    context = context,
                                    webKey = context.getString(R.string.default_web_client_id),
                                    onComplete = this@AuthActivity::login,
                                    onError = { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                ).login()
                            }
                        }
                    ) {
                        Text(
                            text = "구글",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = {
                            KakaoLogin(
                                context = context,
                                onComplete = this@AuthActivity::login,
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            ).login()
                        }
                    ) {
                        Text(
                            text = "카카오",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = {
                            NaverLogin(
                                context = context,
                                clientId = context.getString(R.string.naver_client_id),
                                clientSecret = context.getString(R.string.naver_client_secret),
                                clientName = context.getString(R.string.naver_client_name),
                                onComplete = this@AuthActivity::login,
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            ).login()
                        }
                    ) {
                        Text(
                            text = "네이버",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    private fun login(
        snsKey: String,
        snsType: String,
        email: String,
        nickname: String?,
    ) {
        //TODO request Login Api
    }
}