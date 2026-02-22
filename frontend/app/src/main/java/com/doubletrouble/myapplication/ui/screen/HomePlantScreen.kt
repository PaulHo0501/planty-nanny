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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.data.CacheManager
import com.doubletrouble.myapplication.data.RetrofitClient
import com.doubletrouble.myapplication.ui.component.CustomCard
import com.doubletrouble.myapplication.ui.component.ProgressIndicator
import com.doubletrouble.myapplication.ui.theme.AshBrown
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.ScarletRush
import com.doubletrouble.myapplication.ui.theme.VanillaCream
import com.doubletrouble.myapplication.ui.viewmodel.HomePlantViewModel

@Composable
fun HomePlantScreen(
    viewModel: HomePlantViewModel,
    onNavigateToSoilHumidity: () -> Unit,
    onNavigateToTankWaterLevel: () -> Unit,
    onNavigateToLightStatus: () -> Unit,
    onNavigateToHealthCondition: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val cacheManager = remember { CacheManager(context) }

    var name by remember { mutableStateOf("") }
    var isFactLoading by remember { mutableStateOf(false) }
    var funFact by remember { mutableStateOf("") }

    val soilHumidity = listOf(80, 75, 40, 30, 90, 85)

    LaunchedEffect(Unit) {
        name = cacheManager.getCachedName() ?: ""

        val localFact = cacheManager.getCachedFact()
        if (localFact.isNullOrEmpty()) {
            isFactLoading = true
            try {
                val fetchedFact = RetrofitClient.apiService.getFact()
                funFact = fetchedFact
                cacheManager.setFact(fetchedFact)
            } catch (e: Exception) {
                e.printStackTrace()
                funFact = "Plant love to stay offline sometimes."
            } finally {
                isFactLoading = false
            }
        } else {
            funFact = localFact
            isFactLoading = false
        }

        if (uiState.isLoading && !uiState.isRefreshing) {
            viewModel.fetchDashboardData(isRefresh = false)
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchDashboardData(isRefresh = true) },
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = VanillaCream)
        ) {
            Image(
                painter = painterResource(R.drawable.home_background_plant),
                contentDescription = "Home background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(VanillaCream)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 50.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {


                if (uiState.isLoading) {
                    ProgressIndicator() // Or your custom loading animation
                } else if (uiState.errorMessage != null) {
                    Text(text = uiState.errorMessage!!, color = ScarletRush)
                } else {
                    Text(
                        text = "Your $name is ${if (uiState.healthCondition == "GOOD") "thriving" else "surviving"}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = BlackGrey
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Did you know?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AshBrown
                    )
                    if (isFactLoading) {
                        ProgressIndicator()
                    }
                    else {
                        Text(
                            text = funFact,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BlackGrey
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            CustomCard(
                                title = "Soil Humidity",
                                currentPercentage = uiState.soilHumidity,
                                dataList = soilHumidity,
                                onClick = onNavigateToSoilHumidity
                            )
                        }
                        item {
                            CustomCard(
                                title = "Tank Water Level",
                                currentPercentage = uiState.tankWaterLevel,
                                onClick = onNavigateToTankWaterLevel
                            )
                        }
                        item {
                            CustomCard(
                                title = "Health Condition",
                                status = uiState.healthCondition.replaceFirstChar { it.uppercase() },
                                onClick = onNavigateToHealthCondition
                            )
                        }
                        item {
                            CustomCard(
                                title = "Light",
                                status = uiState.lightStatus.uppercase(),
                                label = "For ${uiState.lightHours} hours",
                                onClick = onNavigateToLightStatus
                            )
                        }
                    }
                }
            }
        }
    }
}
