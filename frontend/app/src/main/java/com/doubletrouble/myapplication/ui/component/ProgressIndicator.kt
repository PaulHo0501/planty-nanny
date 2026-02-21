package com.doubletrouble.myapplication.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.TeaGreen

@Composable
fun ProgressIndicator(progress: Float = 0.5f) {
    Box(
        contentAlignment = Alignment.Center

    ) {
        CircularProgressIndicator(
            progress = {progress},
            color = HunterGreen,
            trackColor = TeaGreen,
            strokeWidth = 4.dp,
        )
    }
}

@Preview(showBackground = true, name = "Progress Indicator Preview")
@Composable
fun ProgressIndicatorPreview() {
    ProgressIndicator(
        progress=0.3f,
    )
}
