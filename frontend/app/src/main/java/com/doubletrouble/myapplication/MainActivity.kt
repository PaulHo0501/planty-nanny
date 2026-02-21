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
import com.doubletrouble.myapplication.ui.screen.HomeNoPlantScreen
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
            WelcomeScreen(onNavigateToHome = { navController.navigate("home") })
        }

        composable("home") {
            HomeNoPlantScreen(onNavigateToAddPlant = { navController.navigate("add_plant") })
        }

        composable("add_plant") {
            AddPlantScreen(onNavigateToAddPlantFinished = {})
        }
    }
}