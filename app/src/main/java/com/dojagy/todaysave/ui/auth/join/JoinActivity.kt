package com.dojagy.todaysave.ui.auth.join

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.data.view.BaseActivity
import com.dojagy.todaysave.data.view.button.BottomButton
import com.dojagy.todaysave.data.view.title.BackTitle
import com.dojagy.todaysave.ui.auth.join.screen.NicknameScreen
import com.dojagy.todaysave.ui.auth.join.screen.TermsCheckScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinActivity : BaseActivity<JoinState, JoinEffect, JoinEvent, JoinViewModel>() {

    override val viewModel: JoinViewModel by viewModels()

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        var inputNickname by rememberSaveable { mutableStateOf(intent.getStringExtra("nickname") ?: String.DEFAULT) }

        val isKeyboardVisible = WindowInsets.isImeVisible

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when(effect) {
                    is JoinEffect.StartOnboard -> {
                        //TODO: 온보드
                    }
                    is JoinEffect.StartTermsWeb -> {
                        //TODO: 약관 웹뷰
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            BackTitle {
                onBack()
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                when(uiState.currentRoute) {
                    JoinRoute.NICKNAME -> {
                        NicknameScreen(
                            nickname = inputNickname,
                            onNicknameChange = {
                                if(it.length < 9) {
                                    inputNickname = it
                                }
                            }
                        )
                    }
                    JoinRoute.TERMS -> {
                        TermsCheckScreen(
                            viewModel = viewModel,
                            onShowTerm = {
                                viewModel.handleEvent(JoinEvent.OnClickShowTerm(it))
                            }
                        ) {
                            viewModel.handleEvent(JoinEvent.OnCheckedTerm(it))
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BottomButton(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    text = when(uiState.currentRoute) {
                        JoinRoute.NICKNAME -> stringResource(R.string.next)
                        JoinRoute.TERMS -> stringResource(R.string.do_join)
                    },
                ) {
                    if(uiState.currentRoute == JoinRoute.NICKNAME) {
                        viewModel.handleEvent(JoinEvent.OnNextButtonClick)
                    }else {
                        viewModel.handleEvent(JoinEvent.OnJoinButtonClick(
                            email = intent.getStringExtra("email") ?: String.DEFAULT,
                            snsType = intent.getStringExtra("snsType") ?: String.DEFAULT,
                            snsKey = intent.getStringExtra("snsKey") ?: String.DEFAULT,
                            nickname = inputNickname
                        ))
                    }
                }

                Spacer(modifier = Modifier.height(
                    if(isKeyboardVisible) {
                        20.dp
                    }else {
                        40.dp
                    }
                ))
            }
        }
    }

    override fun onBack() {
        if(viewModel.uiState.value.currentRoute == JoinRoute.NICKNAME) {
            finish()
        }else {
            viewModel.handleEvent(JoinEvent.OnBack)
        }
    }
}