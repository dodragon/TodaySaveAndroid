package com.dojagy.todaysave.data.view.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dojagy.todaysave.common.android.base.BaseUiEvent
import com.dojagy.todaysave.common.android.base.DismissType
import com.dojagy.todaysave.common.android.base.SnackBarMessage
import com.dojagy.todaysave.common.android.base.SnackBarType
import kotlinx.coroutines.delay

@Composable
fun <T : BaseUiEvent> CustomSnackBarHost(
    messages: List<SnackBarMessage<T>>,
    onDismiss: (messageId: String) -> Unit,
    onAction: (messageId: String, event: T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        messages.forEach { message ->
            // key를 사용하여 각 스낵바의 고유성을 보장 (애니메이션에 필수)
            key(message.id) {
                CustomSnackBarItem(
                    message = message,
                    onDismiss = onDismiss,
                    onAction = onAction
                )
            }
        }

        Spacer(modifier = Modifier.height(56.dp))
    }
}

@Composable
private fun <T : BaseUiEvent> CustomSnackBarItem(
    message: SnackBarMessage<T>,
    onDismiss: (messageId: String) -> Unit,
    onAction: (messageId: String, event: T) -> Unit
) {
    // 자동 소멸 타이머
    if (message.dismissType is DismissType.Automatic) {
        LaunchedEffect(message.id) {
            delay(2000) // 2초 후 자동 닫기
            onDismiss(message.id)
        }
    }

    // 스타일 결정
    val backgroundColor = when (message.type) {
        SnackBarType.Success -> Color(0xFF4CAF50) // 초록
        SnackBarType.Error -> Color(0xFFD32F2F)   // 빨강
        SnackBarType.Normal -> Color(0xFF333333)   // 어두운 회색
    }
    val contentColor = Color.White

    // 애니메이션과 함께 UI 표시
    AnimatedVisibility(
        visible = true, // 항상 true, 리스트에서 제거되면 사라짐
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // 타이틀이 있을 경우에만 표시
                    val titleMessage = message.title
                    titleMessage?.let {
                        Text(
                            text = titleMessage.asString(),
                            color = contentColor,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = message.message.asString(),
                        color = contentColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // DismissType에 따라 다른 버튼 표시
                when (val dismissType = message.dismissType) {
                    is DismissType.Manual -> {
                        IconButton(onClick = { onDismiss(message.id) }) {
                            Icon(Icons.Default.Close, contentDescription = "닫기", tint = contentColor)
                        }
                    }

                    is DismissType.Action -> {
                        TextButton(onClick = { onAction(message.id, dismissType.event) }) {
                            Text(dismissType.actionLabel, color = contentColor, fontWeight = FontWeight.Bold)
                        }
                    }

                    is DismissType.Automatic -> {
                        // 자동 소멸 타입은 버튼 없음
                    }
                }
            }
        }
    }
}