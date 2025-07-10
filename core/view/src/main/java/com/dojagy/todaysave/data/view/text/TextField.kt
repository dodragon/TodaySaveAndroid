package com.dojagy.todaysave.data.view.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray6
import com.dojagy.todaysave.core.resources.theme.pretendard

@Composable
fun TsTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(String.DEFAULT),
    hint: String = String.DEFAULT,
    textSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    hintWeight: FontWeight = fontWeight,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = Gray6,
    hintColor: Color = Gray4,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isSingleLine: Boolean = true,
    maxLine: Int = if (isSingleLine) 1 else Int.MAX_VALUE,
    useUnderLine: Boolean = true,                                   //언더라인 없는 경우 상하 여백도 없음
    onValueChange: (TextFieldValue) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
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

                    TextFieldDecoration(
                        innerTextField = innerTextField,
                        textAlign = textAlign,
                        value = value,
                        isFocused = isFocused,
                        hint = hint,
                        hintColor = hintColor,
                        textSize = textSize,
                        hintWeight = hintWeight
                    )

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
                TextFieldDecoration(
                    innerTextField = innerTextField,
                    textAlign = textAlign,
                    value = value,
                    isFocused = isFocused,
                    hint = hint,
                    hintColor = hintColor,
                    textSize = textSize,
                    hintWeight = hintWeight
                )
            }
        }
    )
}

@Composable
private fun TextFieldDecoration(
    innerTextField: @Composable () -> Unit,
    textAlign: TextAlign,
    value: TextFieldValue,
    isFocused: Boolean,
    hint: String,
    hintColor: Color,
    textSize: TextUnit,
    hintWeight: FontWeight,
) {
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

        if(value.text.isEmpty() && isFocused.not()) {
            TsText(
                text = hint,
                color = hintColor,
                size = textSize,
                fontWeight = hintWeight,
                align = textAlign
            )
        }
    }
}