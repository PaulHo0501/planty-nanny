package com.doubletrouble.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doubletrouble.myapplication.data.CacheManager
import com.doubletrouble.myapplication.data.RetrofitClient
import com.doubletrouble.myapplication.ui.screen.AddPlantScreen
import com.doubletrouble.myapplication.ui.screen.HealthConditionScreen
import com.doubletrouble.myapplication.ui.screen.HomeNoPlantScreen
import com.doubletrouble.myapplication.ui.screen.HomePlantScreen
import com.doubletrouble.myapplication.ui.screen.LightStatusScreen
import com.doubletrouble.myapplication.ui.screen.SoilHumidityScreen
import com.doubletrouble.myapplication.ui.screen.TankWaterLevelScreen
import com.doubletrouble.myapplication.ui.screen.WelcomeScreen
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.viewmodel.AddPlantViewModel
import com.doubletrouble.myapplication.ui.viewmodel.HealthConditionViewModel
import com.doubletrouble.myapplication.ui.viewmodel.LightStatusViewModel
import com.doubletrouble.myapplication.ui.viewmodel.SoilHumidityViewModel
import com.doubletrouble.myapplication.ui.viewmodel.TankWaterLevelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantyNannyTheme {
                PlantyApp()
            }
        }
    }
}

@Composable
fun PlantyApp() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val cacheManager = remember { CacheManager(context) }

    val startDest = if (cacheManager.getCachedName() != null) "home_plant" else "welcome"

    NavHost(navController = navController, startDestination = startDest) {
        composable("welcome") {
            WelcomeScreen(onNavigateToHome = { navController.navigate("home_no_plant") })
        }

        composable("home_no_plant") {
            HomeNoPlantScreen(onNavigateToAddPlant = { navController.navigate("add_plant") })
        }

        composable("add_plant") {
            val plantViewModel: AddPlantViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AddPlantViewModel(RetrofitClient.apiService) as T
                    }
                }
            )
            AddPlantScreen(
                viewModel = plantViewModel,
                onNavigateToAddPlantFinished = {navController.navigate(route = "home_plant") {
                    popUpTo("add_plant") { inclusive = true }
                } }
            )
        }

        composable("home_plant") {
            HomePlantScreen(onNavigateToSoilHumidity = {navController.navigate(route = "soil_humidity")},
                onNavigateToTankWaterLevel = {navController.navigate(route = "tank_water_level")},
                onNavigateToHealthCondition = {navController.navigate(route = "health_condition")},
                onNavigateToLightStatus = {navController.navigate(route = "light_status")})
        }

        composable("soil_humidity") {
            val humidityViewModel: SoilHumidityViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SoilHumidityViewModel(RetrofitClient.apiService) as T
                    }
                }
            )
            SoilHumidityScreen(viewModel = humidityViewModel,
                onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("tank_water_level") {
            val tankWaterLevelViewModel: TankWaterLevelViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TankWaterLevelViewModel(RetrofitClient.apiService) as T
                    }
                }
            )
            TankWaterLevelScreen(viewModel = tankWaterLevelViewModel, onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("health_condition") {
            val healthViewModel: HealthConditionViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HealthConditionViewModel(RetrofitClient.apiService) as T
                    }
                }
            )
            HealthConditionScreen(viewModel = healthViewModel,
                onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("light_status") {
            val lightStatusViewModel: LightStatusViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LightStatusViewModel(RetrofitClient.apiService) as T
                    }
                }
            )
            LightStatusScreen(viewModel = lightStatusViewModel,
                onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }
    }
}