package com.doubletrouble.myapplication.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.data.CacheManager
import com.doubletrouble.myapplication.data.model.Humidity
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.component.Chart
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.MutedOlive
import com.doubletrouble.myapplication.ui.theme.VanillaCream
import com.doubletrouble.myapplication.ui.viewmodel.SoilHumidityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SoilHumidityScreen(viewModel: SoilHumidityViewModel, onNavigateToHomePlant: () -> Unit) {
    val context = LocalContext.current
    val cacheManager = remember { CacheManager(context) }

    var name by remember { mutableStateOf("") }
    var idealSoilHumidity by remember { mutableIntStateOf(0) }

    val dataPoints by viewModel.humidityHistory.collectAsStateWithLifecycle()

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        name = cacheManager.getCachedName() ?: ""
        idealSoilHumidity = cacheManager.getCachedIdealSoilHumidity()
        viewModel.getHumidityHistory()
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            progress.animateTo(1f, animationSpec = tween(durationMillis = 1500))

            // 2. Start the continuous watering loop
            while (isActive) { // This loop automatically dies when the user lets go
                viewModel.triggerWatering()
                delay(3000)
            }
        } else {
            // 3. User let go! Instantly reset the animation
            progress.snapTo(0f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VanillaCream)
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateToHomePlant) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = BlackGrey,
                    modifier = Modifier.size(52.dp)
                )
            }
            Text(
                text = "Humidity",
                style = MaterialTheme.typography.headlineMedium,
                color = BlackGrey
            )
        }

        Column(modifier = Modifier.fillMaxSize()
            .height(48.dp)
            .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = highlightedText("$name ") +
                        AnnotatedString("needs ${getHumidityPreference(idealSoilHumidity)} humidity"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth())
            Text(
                text =  AnnotatedString("Humidity will be checked every ") +
                        highlightedText("30 minutes ") +
                        AnnotatedString("and maintained at ") +
                        highlightedText("$idealSoilHumidity%"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(36.dp))
            Chart(dataPoints = dataPoints.map { it?.percentage ?: 0 },
                showAxis = true,
                labels = formatChartLabels(dataPoints.map { it!! }).filter { !it.isEmpty() },
                height = 360.dp,
                spacedBy = 16.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                icon = ImageVector.vectorResource(id = R.drawable.ic_water),
                text = if (progress.value in 0f..<1f) "Hold to manually water"
                else "Watering in progress . . .",
                onClick = {},
                interactionSource = interactionSource,
                modifier = Modifier.drawWithContent {
                    drawContent()
                    if (progress.value > 0.0f) {
                        val strokeWidth = 6.dp.toPx()
                        val halfStroke = strokeWidth / 2
                        val path = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    left = halfStroke,
                                    top = halfStroke,
                                    right = size.width - halfStroke,
                                    bottom = size.height - halfStroke,
                                    cornerRadius = CornerRadius(24.dp.toPx())
                                )
                            )
                        }
                        val pathMeasure = android.graphics.PathMeasure(path.asAndroidPath(), false)
                        val length = pathMeasure.length
                        val pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(length * progress.value, length),
                            phase = 0f
                        )
                        drawPath(
                            path = path,
                            color = MutedOlive,
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = pathEffect,
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            )
        }
    }
}

private fun formatChartLabels(humidityRecords: List<Humidity>): List<String> {
    val timeRecords = humidityRecords.sortedBy { it.createdAt }.map {it.createdAt}
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeRecords.mapIndexed { index, time ->
        if (index == 1 || index == 3 || index == 5) {
            try {
                val parsedDate = inputFormat.parse(time)
                if (parsedDate != null) {
                    outputFormat.format(parsedDate)
                } else {
                    ""
                }
            } catch (_: Exception) {
                ""
            }
        } else {
            ""
        }
    }
}

private fun getHumidityPreference(idealSoilHumidity : Int) : String {
    return if (idealSoilHumidity >= 50) "high"
    else if (idealSoilHumidity >= 30) "moderate"
    else "low"
}

private fun highlightedText(text: String) : AnnotatedString {
    return AnnotatedString(
        text = text,
        spanStyle = SpanStyle(color = HunterGreen,
            fontWeight = FontWeight.Bold)
    )
}