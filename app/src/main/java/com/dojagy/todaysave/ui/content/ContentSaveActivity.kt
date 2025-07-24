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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.dojagy.todaysave.common.android.util.CustomTabHelper
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.extension.isFalse
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray7
import com.dojagy.todaysave.data.view.clickableNoRipple
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.ui.auth.login.LoginActivity
import com.dojagy.todaysave.util.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Matcher

@AndroidEntryPoint
class ContentSaveActivity : AppBaseActivity<ContentSaveState, ContentSaveEffect, ContentSaveEvent, ContentSaveViewModel>() {

    override val viewModel: ContentSaveViewModel by viewModels()

    private val isMain by lazy {
        intent.getBooleanExtra("isMain", true)
    }

    //조금 친절하게 한다면, 링크 저장 하면서 로그인 체크 하고 다 물고 있다가 로그인 or 회원가입 후 처리 할수도 있긴 함
    override fun viewInit() {
        super.viewInit()
        lifecycleScope.launch {
            if(viewModel.isLogin().isFalse()) {
                startActivity(
                    Intent(this@ContentSaveActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra("message", getString(R.string.need_login_service))
                        putExtra("sharedLink", sharedLink())
                    }
                )
                finish()
            }
        }
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val metadata = uiState.metadata

        val context = LocalContext.current

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
                    .padding(horizontal = 16.dp)
                    .clickableNoRipple {
                        CustomTabHelper.openUrl(
                            context = context,
                            url = metadata?.url ?: String.DEFAULT
                        )
                    },
                text = metadata?.url ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Blue
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
                    .padding(horizontal = 16.dp)
                    .clickableNoRipple {
                        CustomTabHelper.openUrl(
                            context = context,
                            url = metadata?.canonicalUrl ?: String.DEFAULT
                        )
                    },
                text = metadata?.canonicalUrl ?: null.toString(),
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Blue
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if(isMain.isFalse()) {
            sharedLink()?.let { link ->
                viewModel.handleEvent(
                    event = ContentSaveEvent.LinkShared(
                        type = LinkSharedType.APP_SHARE,
                        link = link
                    )
                )
            } ?: run {
                viewModel.showSnackBar("정상적인 링크가 아닙니다.")
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus && isMain.isTrue()) {
            clipboardLink()?.let { link ->
                viewModel.handleEvent(
                    event = ContentSaveEvent.LinkShared(
                        type = LinkSharedType.CLIPBOARD,
                        link = link
                    )
                )
            }
        }
    }

    private fun sharedLink(): String? {
        return if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
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