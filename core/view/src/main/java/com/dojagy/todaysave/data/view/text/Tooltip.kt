package com.dojagy.todaysave.data.view.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray1
import com.dojagy.todaysave.core.resources.theme.Gray6

@Composable
fun TopTooltip(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Gray6,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            TsText(
                modifier = Modifier
                    .padding(12.dp),
                text = text,
                color = Gray1,
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                align = TextAlign.Center
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_tooltip_triangle),
            tint = Gray6,
            contentDescription = null,
        )
    }
}