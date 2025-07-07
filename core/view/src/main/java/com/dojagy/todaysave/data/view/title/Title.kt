package com.dojagy.todaysave.data.view.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.data.view.clickableSingle

@Composable
fun BackTitle(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
                .clickableSingle {
                    onBack()
                },
            painter = painterResource(id = R.drawable.icon_arrow_back),
            contentDescription = "back arrow"
        )
    }
}