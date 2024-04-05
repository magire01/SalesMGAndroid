package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(
    buttonTitle: String,
    onClick: () -> Unit
) {
    OutlinedButton (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 100.dp,
                vertical = 20.dp,
            ),
        onClick = {
            onClick()
        }
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 40.dp),
            text = buttonTitle,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}