package com.doubletrouble.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LightStatusUiState {
    object Idle : LightStatusUiState()
    object Loading : LightStatusUiState()
    data class Success(val lightStatus: String) : LightStatusUiState()
    data class Error(val message: String) : LightStatusUiState()
}

class LightStatusViewModel(private val apiService: MyApiService): ViewModel() {
    private val _uiState = MutableStateFlow<LightStatusUiState>(LightStatusUiState.Idle)
    val uiState: StateFlow<LightStatusUiState> = _uiState.asStateFlow()

    fun postLightStatus(lightStatus : String) {
        viewModelScope.launch {
            _uiState.value = LightStatusUiState.Loading
            try {
                val result = apiService.postLightStatus(
                    cameraId = "esp32_cam_1",
                    lightStatus = lightStatus
                )
                _uiState.value = LightStatusUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LightStatusUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}