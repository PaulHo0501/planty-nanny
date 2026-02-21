package com.doubletrouble.myapplication.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.HunterGreen

@Composable
fun Chart(dataPoints: List<Int>) {
    var animationPlayed by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        dataPoints.forEach { percentage ->
            val height by animateFloatAsState(
                targetValue = if (animationPlayed) percentage / 100f else 0f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 100),
                label = "BarHeight"
            )

            Box(modifier = Modifier.weight(1f)
                .fillMaxHeight(height)
                .background(
                    color = HunterGreen,
                    shape = RoundedCornerShape(50)
                ))
        }
    }
}