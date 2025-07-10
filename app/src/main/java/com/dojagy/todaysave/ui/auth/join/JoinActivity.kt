package com.dojagy.todaysave.ui.auth.join

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.common.android.base.BaseUiEffect
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray6
import com.dojagy.todaysave.core.resources.theme.Red
import com.dojagy.todaysave.data.view.button.FullSizeRoundButton
import com.dojagy.todaysave.data.view.clickableNoRipple
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.data.view.text.TsTextField
import com.dojagy.todaysave.ui.auth.join.screen.TermsCheckBottomSheet
import com.dojagy.todaysave.ui.main.MainActivity
import com.dojagy.todaysave.util.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class JoinActivity : AppBaseActivity<JoinState, JoinEffect, JoinEvent, JoinViewModel>() {

    override val viewModel: JoinViewModel by viewModels()

    @Composable
    override fun Content() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val randomNickname = uiState.randomNickname.asString()
        val selectedNickname = uiState.selectedNickname.asString()
        var inputNickname by remember {
            mutableStateOf(TextFieldValue(String.DEFAULT))
        }

        var bottomBtnEnable by rememberSaveable {
            mutableStateOf(false)
        }

        var isTermsBottomSheetShow by rememberSaveable {
            mutableStateOf(false)
        }

        var requestFocus by rememberSaveable { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(uiState) {
            if (randomNickname.isNotBlank()) {
                inputNickname = TextFieldValue(
                    text = randomNickname,
                    selection = TextRange(randomNickname.length)
                )
                bottomBtnEnable = true
            }

            isTermsBottomSheetShow = uiState.isNicknameChecked && selectedNickname.isNotBlank()
        }

        LaunchedEffect(requestFocus) {
            if (requestFocus) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }

        LaunchedEffect(Unit) {
            requestFocus = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .clickableNoRipple {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
            ) {
                TsText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 80.dp),
                    text = stringResource(R.string.join_title),
                    color = Gray6,
                    size = 20.sp,
                    fontWeight = FontWeight.Bold,
                    align = TextAlign.Center
                )

                TsTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 44.dp),
                    value = inputNickname,
                    focusRequester = focusRequester,
                    hint = stringResource(R.string.join_nickname_hint),
                    onValueChange = {
                        if (it.text.length <= 12) {
                            inputNickname = it
                            viewModel.handleEvent(JoinEvent.OnNicknameChange)
                        }

                        bottomBtnEnable = inputNickname.text.length >= 2
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    textSize = 28.sp,
                    useUnderLine = false
                )

                TsText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = stringResource(R.string.join_nickname_info),
                    color = Gray4,
                    size = 14.sp,
                    fontWeight = FontWeight.Medium,
                    align = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (uiState.nicknameErrorMessage.asString().isNotBlank()) {
                    TsText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = uiState.nicknameErrorMessage.asString(),
                        color = Red,
                        size = 14.sp,
                        fontWeight = FontWeight.Medium,
                        align = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                FullSizeRoundButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = stringResource(R.string.done),
                    enabled = bottomBtnEnable
                ) {
                    viewModel.handleEvent(JoinEvent.OnClickNicknameCheck(inputNickname.text))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            if(isTermsBottomSheetShow) {
                TermsCheckBottomSheet(
                    viewModel = viewModel,
                    onDismiss = {
                        isTermsBottomSheetShow = false
                        viewModel.handleEvent(JoinEvent.OnTermsSheetDismissed)
                    },
                    onClickJoin = {
                        viewModel.handleEvent(JoinEvent.OnClickJoin(
                            email = intent.getStringExtra("email") ?: String.DEFAULT,
                            snsType = intent.getStringExtra("snsType") ?: String.DEFAULT,
                            snsKey = intent.getStringExtra("snsKey") ?: String.DEFAULT,
                            nickname = selectedNickname
                        ))
                    }
                )
            }
        }
    }

    override fun activityHandleEffect(effect: BaseUiEffect) {
        super.activityHandleEffect(effect)
        when (effect) {
            is JoinEffect.StartOnboard -> {
                //TODO: start onboard 일단 테스트로 메인으로 이동하게 하였음
                startActivity(Intent(this@JoinActivity, MainActivity::class.java))
                finish()
            }

            is JoinEffect.StartTermsWeb -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, effect.term.url?.asString(this)?.toUri())
                startActivity(browserIntent)
            }
        }
    }

    override fun onBack() {}
}