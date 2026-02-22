package com.doubletrouble.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SoilHumidityViewModel(private val apiService: MyApiService) : ViewModel() {
    private val _realtimeHumidity = MutableStateFlow<Int?>(null)
    private val _humidityHistory = MutableStateFlow<List<Int>>(emptyList())
    val humidityHistory: StateFlow<List<Int>> = _humidityHistory.asStateFlow()

    fun getHumidityHistory() {
        viewModelScope.launch {
            try {
                val records = apiService.getHumidityHistory()
                val chartData = records.map { it.percentage }.reversed()

                _humidityHistory.value = chartData
                if (chartData.isNotEmpty()) {
                    _realtimeHumidity.value = chartData.last()
                }

            } catch (e: Exception) {
                println("Failed to fetch humidity history: ${e.message}")
            }
        }
    }
}