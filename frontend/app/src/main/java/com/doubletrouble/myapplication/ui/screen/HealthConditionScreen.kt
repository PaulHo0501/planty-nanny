package com.doubletrouble.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.doubletrouble.myapplication.data.CacheManager
import com.doubletrouble.myapplication.data.model.TreeHealth
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.component.ProgressIndicator
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.VanillaCream
import com.doubletrouble.myapplication.ui.viewmodel.HealthConditionViewModel
import com.doubletrouble.myapplication.ui.viewmodel.TreeHealthUiState

@Composable
fun HealthConditionScreen(viewModel : HealthConditionViewModel,
    onNavigateToHomePlant: () -> Unit) {
    val context = LocalContext.current
    val cacheManager = remember { CacheManager(context) }

    val uiState by viewModel.uiState.collectAsState()


    var imageAddress by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var treeHealth by remember { mutableStateOf<TreeHealth?>(null) }

    LaunchedEffect(Unit) {
        name = cacheManager.getCachedName() ?: ""
        viewModel.fetchTreeHealth()
    }

    when (uiState) {
        is TreeHealthUiState.Idle -> {}

        is TreeHealthUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(VanillaCream),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        is TreeHealthUiState.Success -> {
            treeHealth = (uiState as TreeHealthUiState.Success).treeHealth
            imageAddress = treeHealth!!.imageUrl
            val painter = rememberAsyncImagePainter(imageAddress)
            val state by painter.state.collectAsState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = VanillaCream)
                    .padding(vertical = 50.dp),
                verticalArrangement = Arrangement.spacedBy(48.dp)
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
                        text = "Health Condition",
                        style = MaterialTheme.typography.headlineMedium,
                        color = HunterGreen
                    )
                }

                Column(modifier = Modifier.fillMaxSize()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = highlightedText(name) +
                                AnnotatedString(" is in ${treeHealth!!.healthCondition} shape today"),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = treeHealth!!.description,
                        color = HunterGreen,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    when (state) {
                        is AsyncImagePainter.State.Empty,
                        is AsyncImagePainter.State.Loading -> {
                            ProgressIndicator(
                                modifier = Modifier.fillMaxWidth(1f).aspectRatio(1f),
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp)),
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                        }
                    }
                    Text(
                        text = "A photo is taken everyday to diagnose",
                        style = MaterialTheme.typography.labelSmall,
                        color = HunterGreen
                    )
                    Button(text = "Re-diagnose", icon = Icons.Rounded.Refresh, onClick = {
                        viewModel.fetchTreeHealth(manual = true)
                        cacheManager.setHealthCondition(treeHealth!!.healthCondition)
                        cacheManager.setHealthDescription(treeHealth!!.description)
                    })
                }
            }
        }
        is TreeHealthUiState.Error -> {
            val errorMessage = (uiState as TreeHealthUiState.Error).message
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
                    onClick = { viewModel.fetchTreeHealth() }
                )
            }
        }
    }
}

private fun highlightedText(text: String) : AnnotatedString {
    return AnnotatedString(
        text = text,
        spanStyle = SpanStyle(color = HunterGreen,
            fontWeight = FontWeight.Bold)
    )
}