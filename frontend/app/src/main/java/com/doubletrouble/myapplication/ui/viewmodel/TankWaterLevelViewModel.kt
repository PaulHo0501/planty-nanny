package com.doubletrouble.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.BuildConfig
import com.doubletrouble.myapplication.data.MyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.json.JSONObject

sealed class WaterLevelUiState {
    object Idle : WaterLevelUiState()
    object Loading : WaterLevelUiState()
    data class Success(val waterLevel: Int) : WaterLevelUiState()
    data class Error(val message: String) : WaterLevelUiState()
}

class TankWaterLevelViewModel(private val apiService: MyApiService): ViewModel() {
    private val _uiState = MutableStateFlow<WaterLevelUiState>(WaterLevelUiState.Idle)

    val uiState: StateFlow<WaterLevelUiState> = _uiState.asStateFlow()

    private val _realtimeWaterLevel = MutableStateFlow<Int?>(null)
    val realtimeWaterLevel: StateFlow<Int?> = _realtimeWaterLevel.asStateFlow()
    private val baseUrlWs = BuildConfig.BASE_URL_WS

    private val stompClient = StompClient(OkHttpWebSocketClient())

    init {
        connectToWebSocket()
    }

    private fun connectToWebSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val session = stompClient.connect(baseUrlWs)

                launch {
                    session.subscribeText("/topic/water-level").collect { message ->
                        println("STOMP Water Level received: $message")
                        try {
                            val jsonObject = JSONObject(message)
                            val humidityValue = jsonObject.getInt("percentage")
                            _realtimeWaterLevel.value = humidityValue
                        } catch (e: Exception) {
                            println("Failed to parse water level JSON: ${e.message}")
                        }
                    }
                }

            } catch (e: Exception) {
                println("Failed to connect to STOMP: ${e.message}")
            }
        }
    }

    fun getWaterLevel() {
        viewModelScope.launch {
            _uiState.value = WaterLevelUiState.Loading
            try {
                val result = apiService.getWaterLevel()
                _uiState.value = WaterLevelUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = WaterLevelUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}