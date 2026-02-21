package com.doubletrouble.myapplication.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.component.LottieDisplay
import com.doubletrouble.myapplication.ui.theme.AshBrown
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun LightStatusScreen(onNavigateToHomePlant: () -> Unit) {
    val hoursOn = 6
    var lightStatus by remember { mutableStateOf("ON") }

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
                text = "Light Status",
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
                text = highlightedText("Old Lady Cactus ", HunterGreen) +
                        AnnotatedString("loves sunlight and needs ") +
                        highlightedText("8 hours ", HunterGreen) +
                        AnnotatedString("of sunlight everyday to stay happy"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth())
            Text(
                text =  "Sunbathing still in progress . . .",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth())



            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                AnimatedContent(
                    targetState = lightStatus,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(500)) using
                                SizeTransform(clip = false)
                    }
                ) { targetStatus ->
                    LottieDisplay(
                        resId = if (targetStatus == "ON") R.raw.sunny else R.raw.moon,
                        size = 480.dp,
                        onClick = { lightStatus = if (targetStatus == "ON") "OFF" else "ON" })
                }

                if (lightStatus == "ON") {
                    Text(
                        text = AnnotatedString("Light has been ") +
                                highlightedText("ON ", HunterGreen) +
                                AnnotatedString("for $hoursOn hours"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Tap the Sun above to turn off the light manually",
                        style = MaterialTheme.typography.labelSmall
                    )
                } else {
                    Text(
                        text =  AnnotatedString("Light has been ") +
                                highlightedText("OFF ", AshBrown) +
                                AnnotatedString("and will be turn back on tomorrow"),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text =  "Tap the Moon above to turn on the light manually",
                        style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun MutableStateOf(x0: String) {
    TODO("Not yet implemented")
}

private fun highlightedText(text: String, color : Color) : AnnotatedString {
    return AnnotatedString(
        text = text,
        spanStyle = SpanStyle(color = color,
            fontWeight = FontWeight.Bold)
    )
}

@Preview(showBackground = true, name = "Light Status Screen Preview")
@Composable
fun LightStatusScreenPreview() {
    PlantyNannyTheme {
        LightStatusScreen { }
    }
}