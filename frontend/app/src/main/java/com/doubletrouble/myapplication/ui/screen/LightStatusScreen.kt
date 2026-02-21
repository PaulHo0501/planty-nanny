package com.doubletrouble.myapplication.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme

@Composable
fun LightStatusScreen(onNavigateToHomePlant: () -> Unit) {

}

@Preview(showBackground = true, name = "Light Status Screen Preview")
@Composable
fun LightStatusScreenPreview() {
    PlantyNannyTheme {
        LightStatusScreen { }
    }
}