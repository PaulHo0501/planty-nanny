package com.doubletrouble.myapplication.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.doubletrouble.myapplication.R
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme

@Composable
fun LottieDisplay(resId : Int, onClick : () -> Unit, size: Dp? = null) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val interactionSource = remember { MutableInteractionSource() }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(size ?: 200.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

@Preview(showBackground = true, name = "Lottie Display Preview")
@Composable
fun LottieDisplayPreview() {
    PlantyNannyTheme {
        LottieDisplay(resId = R.raw.sunny, onClick = {}, size = 300.dp)
    }
}