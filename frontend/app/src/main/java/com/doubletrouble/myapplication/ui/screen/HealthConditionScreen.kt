package com.doubletrouble.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.component.ProgressIndicator
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun HealthConditionScreen(onNavigateToHomePlant: () -> Unit) {
    val imageAddress = "https://plus.unsplash.com/premium_photo-1725408105248-444e6c1a0576"
    val plantName = "Old Lady Cactus"
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
                text = highlightedText() +
                        AnnotatedString(" is in good health today"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "The spines are dense, evenly white, and fluffy, which is exactly what a healthy Old Lady Cactus should look like.\n" +
                        "No yellowing, browning, or bald patches, those would signal stress or rot.\n" +
                        "The stems look firm and upright, not shriveled or mushy.",
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
                        contentDescription = plantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    // Show some error UI.
                }
            }
            Text(
                text = "A photo is taken everyday to diagnose",
                style = MaterialTheme.typography.labelSmall,
                color = HunterGreen
            )
            Button(text = "Re-diagnose", icon = Icons.Rounded.Refresh, onClick = {})
        }
    }
}

private fun highlightedText() : AnnotatedString {
    return AnnotatedString(
        text = "Old Lady Cactus",
        spanStyle = SpanStyle(color = HunterGreen,
            fontWeight = FontWeight.Bold)
    )
}

@Preview(showBackground = true, name = "Health Condition Screen Preview")
@Composable
fun HealthConditionScreenPreview() {
    PlantyNannyTheme {
        HealthConditionScreen { }
    }
}