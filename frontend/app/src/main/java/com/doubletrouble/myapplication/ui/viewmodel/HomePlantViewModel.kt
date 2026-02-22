package com.doubletrouble.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val soilHumidity: Int = 0,
    val tankWaterLevel: Int = 0,
    val healthCondition: String = "Unknown",
    val lightStatus: String = "OFF",
    val lightHours: Int = 0,
    val errorMessage: String? = null
)

class HomePlantViewModel(private val apiService: MyApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun fetchDashboardData(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            try {
                // 2. Fire all API requests AT THE SAME TIME
                val humidityDeferred = async { apiService.getHumidity() }
                val waterDeferred = async { apiService.getWaterLevel() }
                val healthDeferred = async { apiService.getTreeHealth() }
                val lightStatusDeferred = async { apiService.getTodayLightStatus() }
                val lightHoursDeferred = async { apiService.getTodayLightHours() }



                // 4. Update the UI state all at once
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        soilHumidity = humidityDeferred.await(),
                        tankWaterLevel = waterDeferred.await(),
                        healthCondition = healthDeferred.await().healthCondition,
                        lightStatus = lightStatusDeferred.await(),
                        lightHours = lightHoursDeferred.await()
                    )
                }
            } catch (e: Exception) {
                // If the Spring Boot server is offline or Wi-Fi drops
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = "Failed to connect to server: ${e.message}"
                    )
                }
            }
        }
    }
}