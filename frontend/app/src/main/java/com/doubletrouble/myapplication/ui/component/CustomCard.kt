package com.doubletrouble.myapplication.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.PlantyNannyTheme
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.TeaGreen

@Composable
fun CustomCard(title: String,
               onClick: () -> Unit = {},
               status: String? = null,
               currentPercentage: Int? = null,
               dataList: List<Int>? = null,
               label: String? = null) {
    Card(modifier = Modifier.fillMaxSize()
        .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = TeaGreen),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize().
        padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = BlackGrey
            )
            Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center) {
                Text(
                    text = if (currentPercentage != null) "$currentPercentage%" else status.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = HunterGreen
                )
            }
            if (!dataList.isNullOrEmpty()) {
                Chart(dataList)
            }
            if (!label.isNullOrEmpty()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = HunterGreen
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Custom Card Preview")
@Composable
fun CustomCardPreview() {
    PlantyNannyTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                CustomCard(title = "Soil Humidity",
                    currentPercentage = 12,
                    dataList = listOf(10, 24, 100, 4, 50, 100, 25),
                    onClick = {})
            }
            item {
                CustomCard(title = "Tank Water Level",
                    currentPercentage = 23,
                    onClick = {})
            }
            item {
                CustomCard(title = "Health Condition",
                    status = "Good",
                    onClick = {})
            }
            item {
                CustomCard(title = "Light",
                    status = "ON",
                    label = "For 8 hours",
                    onClick = {})
            }
        }
    }
}