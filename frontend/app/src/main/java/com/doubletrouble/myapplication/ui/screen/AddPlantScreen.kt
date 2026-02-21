package com.doubletrouble.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun AddPlantScreen(onNavigateToAddPlantFinished: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VanillaCream)
    ) {
        Image(
            painter = painterResource(R.drawable.add_plant_background),
            contentDescription = "Home background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .background(VanillaCream)
        )
        Text(
            text = "Identify Your Plant",
            style = MaterialTheme.typography.headlineMedium,
            color = BlackGrey,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 44.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Place your plant onto the base",
                style = MaterialTheme.typography.bodyMedium,
                color = BlackGrey
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                text = "I'm ready",
                icon = Icons.Default.Check,
                onClick = onNavigateToAddPlantFinished
            )
        }
    }
}

@Composable
fun ScanningResult() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VanillaCream)
    ) {
        Image(
            painter = painterResource(R.drawable.add_plant_background),
            contentDescription = "Home background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .background(VanillaCream)
        )
        Column() {
            Text(
                text = "Your Plant is ...",
                style = MaterialTheme.typography.headlineMedium,
                color = BlackGrey,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 44.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Old Lady Cactus",
                        style = MaterialTheme.typography.headlineMedium,
                        color = HunterGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Looks sweet, but absolutely chooses violence",
                        style = MaterialTheme.typography.labelSmall,
                        color = BlackGrey,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(R.drawable.old_lady_cactus),
                    contentDescription = "Old Lady Cactus",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(.9f).aspectRatio(1f)
                        .drawWithCache {
                            val outline = RoundedCornerShape(16.dp).createOutline(size, layoutDirection, this)
                            val path = Path().apply { addOutline(outline) }

                            onDrawBehind {
                                drawPath(
                                    path = path,
                                    color = BlackGrey,
                                    style = Stroke(
                                        width = 2.dp.toPx(),
                                        cap = StrokeCap.Round,  // Smooths out the "clunky" look
                                        join = StrokeJoin.Round, // Ensures corners are balanced
                                        pathEffect = PathEffect.dashPathEffect(
                                            intervals = floatArrayOf(30f, 30f),
                                            phase = 0f
                                        )
                                    )
                                )
                            }
                        }
                        .padding(4.dp) // Optional: adds space between image and border
                        .clip(RoundedCornerShape(16.dp)),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    text = "Continue",
                    onClick = {}
                )
            }
        }

    }
}

@Preview(showBackground = true, name = "Add Plant Screen Preview")
@Composable
fun AddPlantScreenPreview() {
    PlantyNannyTheme {
        //AddPlantScreen({})
        ScanningResult()
    }
}