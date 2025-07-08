package com.dojagy.todaysave.ui.auth.join

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray05
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.data.view.button.FullSizeRoundButton
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.data.view.text.TsTextField
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinActivity : BaseActivity<JoinState, JoinEffect, JoinEvent, JoinViewModel>() {

    override val viewModel: JoinViewModel by viewModels()

    @Composable
    override fun Content() {
        val keyboardController = LocalSoftwareKeyboardController.current

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var inputNickname by rememberSaveable {
            mutableStateOf(uiState.randomNickname)
        }

        var isTermsBottomSheetShow by rememberSaveable {
            mutableStateOf(false)
        }

        var requestFocus by rememberSaveable { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is JoinEffect.NicknameCheckDone -> {
                        isTermsBottomSheetShow = true
                    }
                    is JoinEffect.StartOnboard -> {
                        //TODO: start onboard
                    }
                    is JoinEffect.StartTermsWeb -> {
                        //TODO: start term webview
                    }
                }
            }
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
            ) {
                TsText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 80.dp),
                    text = stringResource(R.string.join_title),
                    color = Gray05,
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
                        inputNickname = it
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    textSize = 28.sp,
                    useUnderLine = false
                )

                //TODO: 기타 등등
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FullSizeRoundButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = stringResource(R.string.done),
                ) {
                    viewModel.handleEvent(JoinEvent.OnClickNicknameCheck(inputNickname))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    override fun onBack() {}
}