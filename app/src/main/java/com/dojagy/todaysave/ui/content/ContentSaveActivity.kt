package com.dojagy.todaysave.ui.content

import android.content.ClipboardManager
import android.content.Intent
import android.util.Patterns
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.common.util.DLog.toIfDataClass
import com.dojagy.todaysave.util.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentSaveActivity : AppBaseActivity<ContentSaveState, ContentSaveEffect, ContentSaveEvent, ContentSaveViewModel>() {

    override val viewModel: ContentSaveViewModel by viewModels()


    //TODO: Splash 없이 진입 가능한 Activity 임
    //TODO: 로그인 체크 필요 -> 로그인 안한 경우 그냥 일단 Login 하라고 던지면 될 듯
    //TODO: 조금 친절하게 한다면, 링크 저장 하면서 로그인 체크 하고 다 물고 있다가 로그인 or 회원가입 후 처리 할수도 있긴 함

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, top = 60.dp),
                text = "링크 : ${sharedLink()}"
            )

            Text(
                modifier = Modifier
                    .padding(start = 20.dp, top = 60.dp),
                text = "메타데이터 : ${uiState.metadata.toIfDataClass()}"
            )
        }
    }

    override fun onBack() {
        finish()
    }


    override fun onResume() {
        super.onResume()

        //아래것들을 가져와서 viewModel로 전달하여 처리하면 될 듯?
        sharedLink()?.let {
            viewModel.requestMetadata(it)
        }
        clipboardLink()
    }

    private fun sharedLink(): String? {
        return if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            urlCheck(intent.getStringExtra(Intent.EXTRA_TEXT))
        }else {
            null
        }
    }

    private fun clipboardLink(): String? {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        if(clipboardManager.hasPrimaryClip() && clipboardManager.primaryClipDescription?.hasMimeType("text/plain").isTrue()) {
            val item = clipboardManager.primaryClip?.getItemAt(0)
            return urlCheck(item?.text?.toString())
        }

        return null
    }

    private fun urlCheck(
        text: String?
    ): String? {
        return if(Patterns.WEB_URL.matcher(text ?: String.DEFAULT).matches()) {
            text
        }else {
            null
        }
    }
}