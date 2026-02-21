package com.doubletrouble.myapplication.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme

@Composable
fun HealthConditionScreen(onNavigateToHomePlant: () -> Unit) {

}

@Preview(showBackground = true, name = "Health Condition Screen Preview")
@Composable
fun HealthConditionScreenPreview() {
    PlantyNannyTheme {
        HealthConditionScreen { }
    }
}