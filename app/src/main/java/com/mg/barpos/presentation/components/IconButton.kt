package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    description: String,
    imageVector: ImageVector,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    action: () -> Unit
) {
    IconButton(onClick = {
        action()
    }) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            modifier = Modifier.size(35.dp),
            tint = tint
        )
    }
}
