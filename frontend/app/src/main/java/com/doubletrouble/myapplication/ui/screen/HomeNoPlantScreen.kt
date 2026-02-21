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
import androidx.compose.material.icons.filled.Add
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
fun HomeNoPlantScreen(onNavigateToAddPlant: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.home_background),
            contentDescription = "Home background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .background(VanillaCream)
        )
        Text(
            text = "Your Plant",
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
                text = "You currently have no plant yet . . .",
                style = MaterialTheme.typography.bodyMedium,
                color = BlackGrey
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(text = "Add", icon = Icons.Default.Add, onClick = onNavigateToAddPlant)
        }
    }
}

@Preview(showBackground = true, name = "Home No Plant Screen Preview")
@Composable
fun HomeScreenPreview() {
    PlantyNannyTheme {
        HomeNoPlantScreen({})
    }
}