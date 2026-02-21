package com.doubletrouble.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.component.CustomCard
import com.doubletrouble.myapplication.ui.theme.AshBrown
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun HomePlantScreen(onNavigateToSoilHumidity : () -> Unit,
                    onNavigateToTankWaterLevel: () -> Unit,
                    onNavigateToLightStatus: () -> Unit,
                    onNavigateToHealthCondition: () -> Unit) {
    val soilHumidity = listOf(80, 75, 40, 30, 90, 85)
    val currentSoilHumidity = 36
    val currentWaterHumidity = 69
    val healthCondition = "Good"
    val lightStatus = "On"
    val lightHours = 8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VanillaCream)
    ) {
        Image(
            painter = painterResource(R.drawable.home_background_plant),
            contentDescription = "Home background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .background(VanillaCream)
        )
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 50.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Your old lady cactus is thriving",
                style = MaterialTheme.typography.headlineMedium,
                color = BlackGrey
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Did you know?",
                style = MaterialTheme.typography.headlineSmall,
                color = AshBrown
            )
            Text(
                text = "The “hair” on an Old Lady Cactus isn’t soft at all — it’s spines acting like sunscreen.",
                style = MaterialTheme.typography.bodyMedium,
                color = BlackGrey
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    CustomCard(title = "Soil Humidity",
                        currentPercentage = currentSoilHumidity,
                        dataList = soilHumidity,
                        onClick = onNavigateToSoilHumidity)
                }
                item {
                    CustomCard(title = "Tank Water Level",
                        currentPercentage = currentWaterHumidity,
                        onClick = onNavigateToTankWaterLevel)
                }
                item {
                    CustomCard(title = "Health Condition",
                        status = healthCondition.replaceFirstChar { it.uppercase() },
                        onClick = onNavigateToHealthCondition)
                }
                item {
                    CustomCard(title = "Light",
                        status = lightStatus.uppercase(),
                        label = "For $lightHours hours",
                        onClick = onNavigateToLightStatus)
                }
            }
        }

    }
}

@Preview(showBackground = true, name = "Main Screen Preview")
@Composable
fun MainScreenPreview() {
    PlantyNannyTheme {
        HomePlantScreen({}, {}, {}, {})
    }
}