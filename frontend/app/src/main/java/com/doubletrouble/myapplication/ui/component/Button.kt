package com.doubletrouble.myapplication.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Button icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, name = "Button Preview")
@Composable
fun PrimaryButtonPreview() {
    PlantyNannyTheme {
        Button(
            text = "Preview", onClick = {}, icon = Icons.Default.Add
        )
    }
}
