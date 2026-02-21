package com.doubletrouble.myapplication.ui.screen

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.data.CacheManager
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.component.ProgressIndicator
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.VanillaCream
import com.doubletrouble.myapplication.ui.viewmodel.AddPlantViewModel
import com.doubletrouble.myapplication.ui.viewmodel.TreeUiState

@Composable
fun AddPlantScreen(
    viewModel: AddPlantViewModel,
    onNavigateToAddPlantFinished: () -> Unit) {
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val cacheManager = remember { CacheManager(context) }

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
        when (uiState) {
            is TreeUiState.Idle -> {
                InitialScanningPrompt(
                    onReadyClick = { viewModel.fetchTree() }
                )
            }

            is TreeUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            is TreeUiState.Success -> {
                val tree = (uiState as TreeUiState.Success).tree
                cacheManager.setName(tree.name)
                cacheManager.setIdealLightHours(tree.lightHours)
                cacheManager.setIdealSoilHumidity(tree.humidityLevel)
                ScanningResult(
                    plantName = tree.name,
                    plantDescription = tree.description,
                    imageAddress = tree.imageURL,
                    onNavigateToAddPlantFinished = onNavigateToAddPlantFinished
                )
            }

            is TreeUiState.Error -> {
                val errorMessage = (uiState as TreeUiState.Error).message
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Oops! Something went wrong:\n$errorMessage",
                        color = BlackGrey,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        text = "Try Again",
                        onClick = { viewModel.fetchTree() }
                    )
                }
            }
        }
    }
}


@Composable
fun InitialScanningPrompt(onReadyClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Identify Your Plant",
            style = MaterialTheme.typography.headlineMedium,
            color = BlackGrey,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 50.dp)
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
                onClick = onReadyClick
            )
        }
    }
}

@Composable
fun ScanningResult(plantName: String,
                   plantDescription: String,
                   imageAddress: String,
                   onNavigateToAddPlantFinished: () -> Unit
) {
    val painter = rememberAsyncImagePainter(imageAddress)
    val state by painter.state.collectAsState()

    Column {
        Text(
            text = "Your Plant is ...",
            style = MaterialTheme.typography.headlineMedium,
            color = BlackGrey,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 50.dp)
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
                    text = plantName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = HunterGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = plantDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BlackGrey,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (state) {
                is AsyncImagePainter.State.Empty,
                is AsyncImagePainter.State.Loading -> {
                    ProgressIndicator(
                        modifier = Modifier.fillMaxWidth(.9f).aspectRatio(1f),
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = plantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f)
                            .drawWithCache {
                                val outline = RoundedCornerShape(16.dp).createOutline(size, layoutDirection, this)
                                val path = Path().apply { addOutline(outline) }

                                onDrawBehind {
                                    drawPath(
                                        path = path,
                                        color = BlackGrey,
                                        style = Stroke(
                                            width = 2.dp.toPx(),
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round,
                                            pathEffect = PathEffect.dashPathEffect(
                                                intervals = floatArrayOf(30f, 30f),
                                                phase = 0f
                                            )
                                        )
                                    )
                                }
                            }
                            .padding(4.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    Log.d("ERROR", "Unable to retrieve image")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                text = "Continue",
                onClick = onNavigateToAddPlantFinished
            )
        }
    }
}