package com.doubletrouble.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Humidity(val id: String,
                    val percentage: Int,
                    val createdAt: String) {
}