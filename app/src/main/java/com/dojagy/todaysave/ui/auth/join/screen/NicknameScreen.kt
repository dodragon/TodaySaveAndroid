package com.dojagy.todaysave.ui.auth.join.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray06
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.data.view.text.UnderlineTextField

@Composable
fun NicknameScreen(
    nickname: String,
    onNicknameChange: (String) -> Unit
) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            )
    ) {
        TsText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = 20.dp),
            text = stringResource(R.string.join_nickname_title),
            color = Gray06,
            size = 24.sp,
            align = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )

        UnderlineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 20.dp),
            value = nickname,
            hint = stringResource(R.string.join_nickname_placeholder),
            onValueChange = onNicknameChange
        )
    }
}