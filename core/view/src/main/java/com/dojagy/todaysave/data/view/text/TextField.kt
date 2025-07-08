package com.dojagy.todaysave.data.view.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.theme.Gray03
import com.dojagy.todaysave.core.resources.theme.Gray05
import com.dojagy.todaysave.core.resources.theme.pretendard

@Composable
fun TsTextField(
    modifier: Modifier = Modifier,
    value: String = String.DEFAULT,
    hint: String = String.DEFAULT,
    textSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    hintWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = Gray05,
    hintColor: Color = Gray03,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isSingleLine: Boolean = true,
    maxLine: Int = if (isSingleLine) 1 else Int.MAX_VALUE,
    useUnderLine: Boolean = true,                                   //언더라인 없는 경우 상하 여백도 없음
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = textSize,
            color = textColor,
            fontFamily = pretendard,
            fontWeight = fontWeight,
            textAlign = textAlign,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        cursorBrush = SolidColor(textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLine,
        maxLines = maxLine,
        decorationBox = if(useUnderLine) {
            { innerTextField ->
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = when(textAlign) {
                            TextAlign.Center -> Alignment.Center
                            TextAlign.End -> Alignment.CenterEnd
                            else -> Alignment.CenterStart
                        }
                    ) {
                        innerTextField()

                        if(value.isEmpty()) {
                            TsText(
                                text = hint,
                                color = hintColor,
                                size = textSize,
                                fontWeight = hintWeight,
                                align = textAlign
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(textColor)
                    )
                }
            }
        }else {
            { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = when(textAlign) {
                        TextAlign.Center -> Alignment.Center
                        TextAlign.End -> Alignment.CenterEnd
                        else -> Alignment.CenterStart
                    }
                ) {
                    innerTextField()

                    if(value.isEmpty()) {
                        TsText(
                            text = hint,
                            color = hintColor,
                            size = textSize,
                            fontWeight = fontWeight,
                            align = textAlign
                        )
                    }
                }
            }
        }
    )
}