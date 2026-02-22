package com.doubletrouble.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import com.doubletrouble.myapplication.data.model.Humidity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SoilHumidityViewModel(private val apiService: MyApiService) : ViewModel() {
    private val _realtimeHumidity = MutableStateFlow<Humidity?>(null)
    private val _humidityHistory = MutableStateFlow<List<Humidity?>>(emptyList())
    val humidityHistory: StateFlow<List<Humidity?>> = _humidityHistory.asStateFlow()

    fun getHumidityHistory() {
        viewModelScope.launch {
            try {
                val records = apiService.getHumidityHistory()
                _humidityHistory.value = records
                if (records.isNotEmpty()) {
                    _realtimeHumidity.value = records.last()
                }

            } catch (e: Exception) {
                println("Failed to fetch humidity history: ${e.message}")
            }
        }
    }

    fun triggerWatering() {
        viewModelScope.launch {
            try {
                val response = apiService.triggerWaterPump()
                if (response.isSuccessful) {
                    Log.d("WATERING", "Water command sent perfectly!")
                } else {
                    Log.e("WATERING ERROR", "Server returned an error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("WATERING ERROR", "Failed to connect to server", e)
            }
        }
    }
}