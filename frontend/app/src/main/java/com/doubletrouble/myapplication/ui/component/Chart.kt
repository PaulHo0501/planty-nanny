package com.doubletrouble.myapplication.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.Gold
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.MutedOlive
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.ScarletRush

@Composable
fun Chart(dataPoints: List<Int>, showAxis: Boolean = false, labels : List<String>? = null) {
    var animationPlayed by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.bodySmall

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(modifier = Modifier.fillMaxWidth()
        .height(100.dp)
        .padding(12.dp)
    ) {
        if (showAxis && !labels.isNullOrEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.dp.toPx()
                val arrowSize = 6.dp.toPx()
                val arrowStyle = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
                val yArrow = Path().apply {
                    moveTo(-arrowSize / 1.5f, arrowSize)
                    lineTo(0f, 0f)
                    lineTo(arrowSize / 1.5f, arrowSize)
                }
                val xArrow = Path().apply {
                    moveTo(size.width - arrowSize, size.height - arrowSize / 1.5f)
                    lineTo(size.width, size.height)
                    lineTo(size.width - arrowSize, size.height + arrowSize / 1.5f)
                }

                drawLine(
                    color = BlackGrey,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 0f, y = size.height),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BlackGrey,
                    start = Offset(x = 0f, y = size.height),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawPath(path = yArrow, color = BlackGrey, style = arrowStyle)
                drawPath(path = xArrow, color = BlackGrey, style = arrowStyle)

                labels.forEachIndexed { index, label ->
                    val xPos = (size.width / 4) * (index + 1)
                    val textLayoutResult = textMeasurer.measure(label, labelStyle)
                    val textWidth = textLayoutResult.size.width

                    drawLine(
                        color = BlackGrey,
                        start = Offset(x = xPos, y = size.height - (4.dp.toPx() / 2)),
                        end = Offset(x = xPos, y = size.height + (4.dp.toPx() / 2)),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x = xPos - (textWidth / 2),
                            y = size.height + 4.dp.toPx()
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxSize()
                .padding(if (showAxis && !labels.isNullOrEmpty()) 12.dp else 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            dataPoints.forEach { percentage ->
                val height by animateFloatAsState(
                    targetValue = if (animationPlayed) (percentage / 100f).coerceAtLeast(0.15f) else 0f,
                    animationSpec = tween(durationMillis = 1000, delayMillis = 100),
                    label = "BarHeight"
                )

                Box(modifier = Modifier.weight(1f)
                    .fillMaxHeight(height)
                    .background(
                        color = if (!showAxis) HunterGreen else chooseColor(percentage),
                        shape = RoundedCornerShape(50)
                    ))
            }
        }
    }
}

fun chooseColor(height : Int): Color {
    return if (height > 70f) MutedOlive
    else if (height > 40f) Gold
    else ScarletRush
}

@Preview(showBackground = true, name = "Chart Preview")
@Composable
fun ChartPreview() {
    PlantyNannyTheme {
        Box(modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center) {
            Chart(dataPoints = listOf(10, 20, 30, 40, 90, 60, 70),
                showAxis = true,
                labels = listOf("1 PM", "2 PM", "3 PM"))
        }
    }
}