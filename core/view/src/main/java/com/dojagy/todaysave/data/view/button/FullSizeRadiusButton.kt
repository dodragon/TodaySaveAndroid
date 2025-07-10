package com.dojagy.todaysave.data.view.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.common.extension.isTrue
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray7
import com.dojagy.todaysave.data.view.CLICK_INTERVAL
import com.dojagy.todaysave.data.view.text.TsText

@Composable
fun FullSizeRadiusButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isSingleClick: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Gray7,
        disabledContainerColor = Gray4
    ),
    onClick: () -> Unit
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = colors,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if(isSingleClick.isTrue()) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > CLICK_INTERVAL) {
                    lastClickTime = currentTime
                    onClick()
                }
            }else {
                onClick()
            }
        }
    ) {
        TsText(
            modifier = Modifier
                .padding(vertical = 18.dp),
            text = text,
            align = TextAlign.Center,
            color = Color.White,
            size = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}