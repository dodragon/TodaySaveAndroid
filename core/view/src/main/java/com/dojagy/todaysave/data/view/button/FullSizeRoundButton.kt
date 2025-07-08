package com.dojagy.todaysave.data.view.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray7
import com.dojagy.todaysave.data.view.text.TsText

@Composable
fun FullSizeRoundButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Gray7,
        disabledContainerColor = Gray4
    ),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = colors,
        enabled = enabled,
        shape = RoundedCornerShape(32.dp),
        onClick = onClick
    ) {
        TsText(
            text = text,
            align = TextAlign.Center,
            color = Color.White,
            size = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}