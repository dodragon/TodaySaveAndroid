package com.dojagy.todaysave.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.common.util.DLog.toIfDataClass
import com.dojagy.todaysave.core.resources.theme.TodaySaveTheme
import com.dojagy.todaysave.data.view.button.FullSizeRoundButton
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.ui.auth.login.LoginActivity
import com.dojagy.todaysave.ui.content.ContentSaveActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    //TODO: 회원가입이나 로그인에서 전달 받은 sharedLink intent Extra 처리 필요

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            TodaySaveTheme {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val user by viewModel.user.collectAsStateWithLifecycle()

                    TsText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp)
                            .padding(horizontal = 16.dp),
                        text = user.toIfDataClass()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    FullSizeRoundButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = "컨텐츠 저장하기"
                    ) {
                        startActivity(
                            Intent(context, ContentSaveActivity::class.java)
                                .putExtra("isMain", true)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    FullSizeRoundButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = "로그아웃"
                    ) {
                        viewModel.logout()
                        startActivity(Intent(context, LoginActivity::class.java))
                        finish()
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}