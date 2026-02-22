package com.doubletrouble.myapplication.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.MutedOlive
import com.doubletrouble.myapplication.ui.theme.ScarletRush
import com.doubletrouble.myapplication.ui.theme.TeaGreen
import com.doubletrouble.myapplication.ui.theme.VanillaCream
import com.doubletrouble.myapplication.ui.theme.VibrantCoral
import com.doubletrouble.myapplication.ui.viewmodel.TankWaterLevelViewModel
import com.doubletrouble.myapplication.ui.viewmodel.WaterLevelUiState

@Composable
fun TankWaterLevelScreen(viewModel: TankWaterLevelViewModel, onNavigateToHomePlant: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val realtimeWaterLevel by viewModel.realtimeWaterLevel.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition()
    val waveOffsetFront by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(LocalDensity.current) { -200.dp.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )
    val waveOffsetBack by infiniteTransition.animateFloat(
        initialValue = with(LocalDensity.current) { -200.dp.toPx() },
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        )
    )
    val bottlePainter = painterResource(id = R.drawable.water_bottle)
    var originalFillPercentage by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getWaterLevel()
    }

    LaunchedEffect(uiState) {
        originalFillPercentage = if (uiState is WaterLevelUiState.Success) {
            (uiState as WaterLevelUiState.Success).waterLevel
        } else {
            0
        }
    }

    LaunchedEffect(realtimeWaterLevel) {
        realtimeWaterLevel?.let { incomingWaterLevel: Int ->
            originalFillPercentage = incomingWaterLevel
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VanillaCream)
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToHomePlant) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = BlackGrey,
                    modifier = Modifier.size(52.dp)
                )
            }
            Text(
                text = "Tank Water Level",
                style = MaterialTheme.typography.headlineMedium,
                color = BlackGrey
            )
        }

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.water_bottle),
                    contentDescription = "Welcome background",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth()
                        .height(360.dp),
                )
                Canvas(modifier = Modifier
                    .size(200.dp, 360.dp)
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                ) {
                    with(bottlePainter) {
                        draw(size = size)
                    }

                    val fillLevel = size.height * (1f - (originalFillPercentage / 100f))
                    val waveHeightFront = 32.dp.toPx()
                    val waveHeightBack = 24.dp.toPx()

                    val wavePathFront = Path().apply {
                        moveTo(waveOffsetFront, fillLevel)

                        for (i in 0..1) {
                            val startX = waveOffsetFront + (i * size.width)

                            quadraticTo(
                                startX + size.width * 0.25f, fillLevel - waveHeightFront,
                                startX + size.width * 0.5f, fillLevel
                            )
                            quadraticTo(
                                startX + size.width * 0.75f, fillLevel + waveHeightFront,
                                startX + size.width, fillLevel
                            )
                        }
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    val wavePathBack = Path().apply {
                        moveTo(waveOffsetBack, fillLevel)

                        for (i in 0..1) {
                            val startX = waveOffsetBack + (i * size.width)
                            quadraticTo(
                                startX + size.width * 0.25f, fillLevel + waveHeightBack,
                                startX + size.width * 0.5f, fillLevel
                            )
                            quadraticTo(
                                startX + size.width * 0.75f, fillLevel - waveHeightBack,
                                startX + size.width, fillLevel
                            )
                        }
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(
                        path = wavePathBack,
                        color = chooseColor(originalFillPercentage)[0],
                        blendMode = BlendMode.SrcIn
                    )
                    drawPath(
                        path = wavePathFront,
                        color = chooseColor(originalFillPercentage)[1],
                        blendMode = BlendMode.SrcIn
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$originalFillPercentage%",
                style = MaterialTheme.typography.headlineLarge,
                color = BlackGrey
            )
            Spacer(modifier = Modifier.height(52.dp))
            if (originalFillPercentage < 10) {
                Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(ScarletRush)) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = VanillaCream,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Please refill",
                            style = MaterialTheme.typography.bodyMedium,
                            color = VanillaCream
                        )
                    }
                }
            }

        }
    }
}

fun chooseColor(fillPercentage : Int) : List<Color> {
    return if (fillPercentage > 50) {
        listOf(TeaGreen, MutedOlive)
    } else {
        listOf(VibrantCoral, ScarletRush)
    }
}