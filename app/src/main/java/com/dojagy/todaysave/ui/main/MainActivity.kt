package com.dojagy.todaysave.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dojagy.todaysave.core.resources.theme.TodaySaveTheme
import com.dojagy.todaysave.data.view.button.FullSizeRoundButton
import com.dojagy.todaysave.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            TodaySaveTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    FullSizeRoundButton(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 36.dp),
                        text = "로그아웃"
                    ) {
                        viewModel.logout()
                        startActivity(Intent(context, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}