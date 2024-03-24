package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent

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