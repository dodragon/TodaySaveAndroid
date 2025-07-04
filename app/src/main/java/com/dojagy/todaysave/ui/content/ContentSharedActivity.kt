package com.dojagy.todaysave.ui.content

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentSharedActivity : ComponentActivity() {

    //TODO: Splash 없이 진입 가능한 Activity 임
    //TODO: 로그인 체크 필요 -> 로그인 안한 경우 그냥 일단 Login 하라고 던지면 될 듯
    //TODO: 조금 친절하게 한다면, 링크 저장 하면서 로그인 체크 하고 다 물고 있다가 로그인 or 회원가입 후 처리 할수도 있긴 함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(
                "링크 : ${sharedLink()}"
            )
        }
    }

    private fun sharedLink(): String? {
        return if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        }else {
            null
        }
    }
}