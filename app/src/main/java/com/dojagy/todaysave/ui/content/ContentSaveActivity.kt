package com.dojagy.todaysave.ui.content

import android.content.ClipboardManager
import android.content.Intent
import android.util.Patterns
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.common.util.DLog
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray7
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.util.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher

@AndroidEntryPoint
class ContentSaveActivity : AppBaseActivity<ContentSaveState, ContentSaveEffect, ContentSaveEvent, ContentSaveViewModel>() {

    override val viewModel: ContentSaveViewModel by viewModels()


    //TODO: Splash 없이 진입 가능한 Activity 임
    //TODO: 로그인 체크 필요 -> 로그인 안한 경우 그냥 일단 Login 하라고 던지면 될 듯
    //TODO: 조금 친절하게 한다면, 링크 저장 하면서 로그인 체크 하고 다 물고 있다가 로그인 or 회원가입 후 처리 할수도 있긴 함

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val metadata = uiState.metadata

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "공유한 링크",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            TsText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                text = metadata?.url ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray7
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "캐노니컬 링크",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            TsText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                text = metadata?.canonicalUrl ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray7
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "썸네일",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                model = metadata?.thumbnailUrl,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "파비콘",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 16.dp),
                model = metadata?.faviconUrl,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "타이틀",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            TsText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                text = metadata?.title ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray7
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "디스크립션",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            TsText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                text = metadata?.description ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray7
            )

            Spacer(modifier = Modifier.height(24.dp))

            TsText(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "링크타입",
                size = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Gray4
            )

            TsText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                text = metadata?.linkType ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray7
            )

            Spacer(modifier = Modifier.height(60.dp))
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
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            DLog.e("sharedText", sharedText)
            urlCheck(sharedText)
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
        val matcher: Matcher = Patterns.WEB_URL.matcher(text ?: String.DEFAULT)
        return if(matcher.find()) {
            matcher.group()
        }else {
            null
        }
    }
}