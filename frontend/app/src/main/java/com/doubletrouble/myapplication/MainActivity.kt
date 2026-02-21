package com.doubletrouble.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doubletrouble.myapplication.ui.screen.AddPlantScreen
import com.doubletrouble.myapplication.ui.screen.HealthConditionScreen
import com.doubletrouble.myapplication.ui.screen.HomeNoPlantScreen
import com.doubletrouble.myapplication.ui.screen.HomePlantScreen
import com.doubletrouble.myapplication.ui.screen.LightStatusScreen
import com.doubletrouble.myapplication.ui.screen.SoilHumidityScreen
import com.doubletrouble.myapplication.ui.screen.TankWaterLevelScreen
import com.doubletrouble.myapplication.ui.screen.WelcomeScreen
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme

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

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(onNavigateToHome = { navController.navigate("home_no_plant") })
        }

        composable("home_no_plant") {
            HomeNoPlantScreen(onNavigateToAddPlant = { navController.navigate("add_plant") })
        }

        composable("add_plant") {
            AddPlantScreen(onNavigateToAddPlantFinished = {navController.navigate(route = "home_plant")})
        }

        composable("home_plant") {
            HomePlantScreen(onNavigateToSoilHumidity = {navController.navigate(route = "soil_humidity")},
                onNavigateToTankWaterLevel = {navController.navigate(route = "tank_water_level")},
                onNavigateToHealthCondition = {navController.navigate(route = "health_condition")},
                onNavigateToLightStatus = {navController.navigate(route = "light_status")})
        }

        composable("soil_humidity") {
            SoilHumidityScreen(onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("tank_water_level") {
            TankWaterLevelScreen(onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("health_condition") {
            HealthConditionScreen(onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }

        composable("light_status") {
            LightStatusScreen(onNavigateToHomePlant = {navController.navigate(route = "home_plant")})
        }
    }
}