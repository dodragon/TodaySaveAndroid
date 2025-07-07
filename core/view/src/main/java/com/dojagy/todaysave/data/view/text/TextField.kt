package com.dojagy.todaysave.data.view.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.theme.Gray03
import com.dojagy.todaysave.core.resources.theme.Gray05
import com.dojagy.todaysave.core.resources.theme.pretendard

@Composable
fun UnderlineTextField(
    modifier: Modifier = Modifier,
    value: String = String.DEFAULT,
    hint: String = String.DEFAULT,
    textSize: TextUnit = 14.sp,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = textSize,
            color = Gray05,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Start
        ),
        placeholder = {
            TsText(
                text = hint,
                color = Gray03,
                size = textSize,
                fontWeight = FontWeight.Medium,
                align = TextAlign.Start
            )
        },
        // 색상 커스터마이징
        colors = TextFieldDefaults.colors(
            // 텍스트 필드의 배경을 투명하게 설정
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            // 커서 색상을 밑줄 색상과 동일하게 설정
            cursorColor = Gray05,

            // 포커스 됐을 때와 안됐을 때의 밑줄 색상 설정
            focusedIndicatorColor = Gray05,
            unfocusedIndicatorColor = Gray05,
            disabledIndicatorColor = Gray05, // 비활성화 시 밑줄 색상
        ),
        // 한 줄 입력만 받도록 설정
        singleLine = true
    )
}