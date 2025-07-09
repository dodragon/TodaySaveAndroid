package com.dojagy.todaysave.data.view.checkbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray6
import com.dojagy.todaysave.data.view.clickableNoRipple

@Composable
fun TsCheckbox(
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.checkbox_anim)
    )
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(isChecked) {
        if (isChecked) {
            // isChecked가 true가 되면, 애니메이션을 처음부터 끝까지 재생합니다.
            // animate() 함수가 끝나면 lottieAnimatable.progress는 1f(마지막 프레임)에 머무릅니다.
            lottieAnimatable.animate(
                composition = composition,
                initialProgress = 0f
            )
        } else {
            // isChecked가 false가 되면, (숨겨진) 로티를 즉시 첫 프레임으로 되돌립니다.
            // 다음 애니메이션을 위해 준비하는 과정입니다.
            lottieAnimatable.snapTo(composition = composition, progress = 0f)
        }
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .clickableNoRipple {
                onCheckedChange(!isChecked)
            },
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            // isChecked가 true이면, 항상 LottieAnimation을 보여줍니다.
            // LaunchedEffect가 애니메이션 재생/정지를 모두 처리해줍니다.
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                progress = { lottieAnimatable.progress }, // 현재 진행 상태를 그대로 반영
                contentScale = ContentScale.Fit
            )
        } else {
            // isChecked가 false이면, 항상 'off' 상태의 아이콘을 보여줍니다.
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.ic_checkbox_off),
                tint = Gray4, // tint 색상은 필요에 따라 조절
                contentDescription = "checkbox_off"
            )
        }
    }
}