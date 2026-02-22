package com.doubletrouble.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TreeHealth(val imageUrl : String,
                      val healthCondition : Boolean,
                      val description : String)