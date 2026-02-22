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
    data class SuccessPost(val lightStatus: String) : LightStatusUiState()
    data class  SuccessGet(val lightHours: Int) : LightStatusUiState()

    data class  SuccessGetLightStatus(val lightStatus: String) : LightStatusUiState()
    data class Error(val message: String) : LightStatusUiState()
}

class LightStatusViewModel(private val apiService: MyApiService): ViewModel() {
    private val _uiStatePost = MutableStateFlow<LightStatusUiState>(LightStatusUiState.Idle)
    private val _uiStateGet = MutableStateFlow<LightStatusUiState>(LightStatusUiState.Idle)
    private val _uiStateGetLightStatus = MutableStateFlow<LightStatusUiState>(LightStatusUiState.Idle)


    val uiStatePost: StateFlow<LightStatusUiState> = _uiStatePost.asStateFlow()
    val uiStateGet: StateFlow<LightStatusUiState> = _uiStateGet.asStateFlow()
    val uiStateGetLightStatus :  StateFlow<LightStatusUiState> = _uiStateGetLightStatus.asStateFlow()

    fun getTodayLightHours() {
        viewModelScope.launch {
            _uiStateGet.value = LightStatusUiState.Loading
            try {
                val result = apiService.getTodayLightHours()
                _uiStateGet.value = LightStatusUiState.SuccessGet(result)
            } catch (e: Exception) {
                _uiStateGet.value = LightStatusUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        } 
    }

    fun getTodayLightStatus() {
        viewModelScope.launch {
            _uiStateGetLightStatus.value = LightStatusUiState.Loading
            try {
                val result = apiService.getTodayLightStatus()
                _uiStateGetLightStatus.value = LightStatusUiState.SuccessGetLightStatus(result)
            } catch (e: Exception) {
                _uiStateGetLightStatus.value = LightStatusUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
    fun postLightStatus(lightStatus : String) {
        viewModelScope.launch {
            _uiStatePost.value = LightStatusUiState.Loading
            try {
                val result = apiService.postLightStatus(
                    cameraId = "esp32_cam_1",
                    lightStatus = lightStatus
                )
                _uiStatePost.value = LightStatusUiState.SuccessPost(result)
            } catch (e: Exception) {
                _uiStatePost.value = LightStatusUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}