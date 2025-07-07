package com.dojagy.todaysave.ui.auth.join.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dojagy.todaysave.core.resources.R
import com.dojagy.todaysave.core.resources.theme.Gray02
import com.dojagy.todaysave.core.resources.theme.Gray04
import com.dojagy.todaysave.core.resources.theme.Gray06
import com.dojagy.todaysave.data.view.clickableSingle
import com.dojagy.todaysave.data.view.text.TsText
import com.dojagy.todaysave.ui.auth.join.JoinViewModel
import com.dojagy.todaysave.ui.auth.join.Term
import com.dojagy.todaysave.ui.auth.join.TermsCheck

@Composable
fun TermsCheckScreen(
    viewModel: JoinViewModel,
    onShowTerm: (Term) -> Unit,
    onCheckChange: (TermsCheck) -> Unit
) {
    val termsList by viewModel.terms.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            )
    ) {
        TsText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = 20.dp),
            text = stringResource(R.string.join_terms_title),
            color = Gray06,
            size = 24.sp,
            align = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        termsList.forEach { termsCheck ->
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(
                        color = Gray02,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickableSingle {
                        onCheckChange(termsCheck.copy(isCheck = termsCheck.isCheck.not()))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))

                Checkbox(
                    modifier = Modifier
                        .size(16.dp),
                    checked = termsCheck.isCheck,
                    onCheckedChange = {
                        onCheckChange(termsCheck.copy(isCheck = termsCheck.isCheck.not()))
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                TsText(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    text = termsCheck.term.title,
                    color = Gray06,
                    size = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

                TsText(
                    modifier = Modifier
                        .clickableSingle {
                            onShowTerm(termsCheck.term)
                        },
                    text = stringResource(R.string.go_to_check),
                    color = Gray04,
                    size = 12.sp,
                    fontWeight = FontWeight.Normal,
                    align = TextAlign.End
                )

                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}