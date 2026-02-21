package com.doubletrouble.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tree(
    val id: String,
    val name: String,
    val description: String,
    val imageURL: String,
    @SerialName("humidity_level") val humidityLevel: Int,
    @SerialName("light_hours") val lightHours: Int
)