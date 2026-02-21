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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun AddPlantScreen(onNavigateToAddPlantFinished: () -> Unit) {
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
        Text(
            text = "Identify Your Plant",
            style = MaterialTheme.typography.headlineMedium,
            color = BlackGrey,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 44.dp)
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
                onClick = onNavigateToAddPlantFinished
            )
        }
    }
}

@Preview(showBackground = true, name = "Add Plant Screen Preview")
@Composable
fun AddPlantScreenPreview() {
    PlantyNannyTheme {
        AddPlantScreen({})
    }
}