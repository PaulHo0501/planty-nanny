package com.doubletrouble.myapplication.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = HunterGreen,
    secondary = MutedOlive,
    tertiary = TeaGreen,
    background = VanillaCream,
)

@Composable
fun PlantyNannyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = CustomTypography,
        content = content
    )
}