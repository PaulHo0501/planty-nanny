package com.doubletrouble.myapplication.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doubletrouble.myapplication.ui.theme.BlackGrey
import com.doubletrouble.myapplication.ui.theme.HunterGreen
import com.doubletrouble.myapplication.ui.theme.TeaGreen

@Composable
fun CustomCard(title: String,
               status: String? = null,
               currentPercentage: Int? = null,
               dataList: List<Int>? = null,
               label: String? = null) {
    Card(modifier = Modifier.fillMaxSize()
        .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = TeaGreen)
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
            Spacer(modifier = Modifier.height(if(dataList.isNullOrEmpty()) 40.dp else 24.dp))
            Text(
                text = if (currentPercentage != null) "$currentPercentage%" else status.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = HunterGreen
            )
            if (!dataList.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Chart(dataList)
            }
            if (!label.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(36.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = HunterGreen
                )
            }
        }
    }
}