package com.doubletrouble.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.component.Button
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.VanillaCream

@Composable
fun WelcomeScreen(onNavigateToHome: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VanillaCream),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_background),
            contentDescription = "Welcome background",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome",
                color = BlackGrey,
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                text = "Begin",
                onClick = onNavigateToHome
            )
        }
    }
}

@Preview(showBackground = true, name = "Welcome Screen Preview")
@Composable
fun WelcomeScreenPreview() {
    PlantyNannyTheme {
        WelcomeScreen({})
    }
}