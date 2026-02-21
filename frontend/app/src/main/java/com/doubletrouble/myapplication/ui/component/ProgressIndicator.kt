package com.doubletrouble.myapplication.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.TeaGreen

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier, ) {
    Box(
        modifier = modifier,

    ) {
        CircularProgressIndicator(
            color = HunterGreen,
            trackColor = TeaGreen,
            strokeWidth = 12.dp,
            modifier = Modifier.fillMaxWidth(.6f).aspectRatio(1f).align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, name = "Progress Indicator Preview")
@Composable
fun ProgressIndicatorPreview() {
    ProgressIndicator(
    )
}
