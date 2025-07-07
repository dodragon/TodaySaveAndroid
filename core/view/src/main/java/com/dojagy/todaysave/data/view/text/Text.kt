package com.dojagy.todaysave.data.view.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.core.resources.R

@Composable
fun TsText(
    modifier: Modifier = Modifier,
    text: String,
    size: TextUnit = 14.sp,
    color: Color = Color.Black,
    align: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = size,
        color = color,
        textAlign = align,
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        fontFamily = FontFamily(
            Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
            Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
            Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
        ),
        fontWeight = fontWeight
    )
}

@Composable
fun TsAnnotatedText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    size: TextUnit = 14.sp,
    color: Color = Color.Black,
    align: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = size,
        color = color,
        textAlign = align,
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        fontFamily = FontFamily(
            Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
            Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
            Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
        ),
        fontWeight = fontWeight
    )
}