@file:OptIn(ExperimentalMaterial3Api::class)

package com.dojagy.todaysave.data.view.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dojagy.todaysave.core.resources.theme.Gray1
import com.dojagy.todaysave.core.resources.theme.Gray7
import kotlinx.coroutines.launch

@Composable
fun TsBottomSheet(
    modifier: Modifier = Modifier,
    isUserCloseable: Boolean = false,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.(onClose: () -> Unit) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth(),
        sheetState = sheetState,
        onDismissRequest = {
            if(isUserCloseable) {
                onDismiss()
            }
        },
        dragHandle = {},
        containerColor = Gray1,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        scrimColor = Gray7.copy(alpha = 0.2f),
    ) {
        content {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) {
                    onDismiss()
                }
            }
        }
    }
}