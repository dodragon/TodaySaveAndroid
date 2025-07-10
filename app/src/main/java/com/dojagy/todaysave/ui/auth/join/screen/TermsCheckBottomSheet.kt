package com.dojagy.todaysave.ui.auth.join.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray1
import com.dojagy.todaysave.core.resources.theme.Gray2
import com.dojagy.todaysave.core.resources.theme.Gray3
import com.dojagy.todaysave.core.resources.theme.Gray4
import com.dojagy.todaysave.core.resources.theme.Gray5
import com.dojagy.todaysave.core.resources.theme.Gray6
import com.dojagy.todaysave.data.view.bottom_sheet.TsBottomSheet
import com.dojagy.todaysave.data.view.button.FullSizeRadiusButton
import com.dojagy.todaysave.data.view.checkbox.TsCheckbox
import com.dojagy.todaysave.data.view.clickableNoRipple
import com.dojagy.todaysave.data.view.clickableSingle
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.ui.auth.join.CheckTermType
import com.dojagy.todaysave.ui.auth.join.JoinEvent
import com.dojagy.todaysave.ui.auth.join.JoinViewModel
import com.dojagy.todaysave.ui.auth.join.Term

@Composable
fun TermsCheckBottomSheet(
    viewModel: JoinViewModel,
    onDismiss: () -> Unit,
    onClickJoin: () -> Unit
) {
    val termsList by viewModel.terms.collectAsStateWithLifecycle()

    val isAllChecked = remember(termsList) { termsList.all { it.isCheck } }

    var bottomButtonEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(termsList) {
        var isAllChecked = true
        termsList.forEach {
            if(it.isCheck.not()) {
                isAllChecked = false
                return@forEach
            }
        }
        bottomButtonEnabled = isAllChecked
    }

    TsBottomSheet(
        isUserCloseable = true,
        onDismiss = onDismiss,
    ) { onClose ->
        TsText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.join_term_bottom_sheet_title),
            color = Gray6,
            size = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(28.dp))

        TermsAllCheck(
            isChecked = isAllChecked,
            onCheckedChange = { newCheckedState ->
                viewModel.handleEvent(JoinEvent.OnClickTermsCheck(
                    type = CheckTermType.ALL,
                    isCheck = newCheckedState
                ))
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        termsList.forEach { termCheck ->
            TermsNormalCheck(
                term = termCheck.term,
                isChecked = termCheck.isCheck,
                onCheckedChange = {
                    viewModel.handleEvent(JoinEvent.OnClickTermsCheck(
                        type = termCheck.term.type,
                        isCheck = it
                    ))
                },
                onClickTermShow = { url ->
                    viewModel.handleEvent(JoinEvent.OnClickShowTerm(
                        term = termCheck.term
                    ))
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        FullSizeRadiusButton(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.join_complete),
            enabled = bottomButtonEnabled,
            onClick = onClickJoin
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun TermsAllCheck(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = if (isPressed) {
        if (isChecked) Gray1 else Gray2
    } else {
        if (isChecked) Gray2 else Gray1
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(
                RoundedCornerShape(4.dp)
            )
            .background(
                color = backgroundColor
            )
            .border(
                width = 1.dp,
                color = Gray3,
                shape = RoundedCornerShape(4.dp)
            )
            .clickableNoRipple {
                onCheckedChange(!isChecked)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))

        TermsCheckText(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f),
            termText = stringResource(R.string.all_check),
            isChecked = isChecked
        )
    }
}

@Composable
fun TermsNormalCheck(
    modifier: Modifier = Modifier,
    term: Term,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickTermShow: (url: String) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
                .clickableNoRipple {
                    onCheckedChange(!isChecked)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(28.dp))

            TermsCheckText(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f),
                termText = term.text.asString(),
                isChecked = isChecked
            )
        }

        if(term.url != null) {
            TsText(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 28.dp)
                    .clickableSingle {
                        onClickTermShow(term.url.asString(context))
                    },
                text = stringResource(R.string.show),
                size = 14.sp,
                color = Gray4,
                fontWeight = FontWeight.Medium,
                align = TextAlign.Center
            )
        }
    }
}

@Composable
fun TermsCheckText(
    modifier: Modifier = Modifier,
    termText: String,
    isChecked: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TsCheckbox(
            isChecked = isChecked,
        )

        Spacer(modifier = Modifier.width(10.dp))

        TsText(
            text = termText,
            color = Gray5,
            size = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

