package com.doubletrouble.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import com.doubletrouble.myapplication.data.model.TreeHealth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class TreeHealthUiState {
    object Idle : TreeHealthUiState()
    object Loading : TreeHealthUiState()
    data class Success(val treeHealth: TreeHealth) : TreeHealthUiState()
    data class Error(val message: String) : TreeHealthUiState()
}

class HealthConditionViewModel(private val apiService: MyApiService) : ViewModel() {

    private val _uiState = MutableStateFlow<TreeHealthUiState>(TreeHealthUiState.Idle)
    val uiState: StateFlow<TreeHealthUiState> = _uiState.asStateFlow()

    fun fetchTreeHealth(manual: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = TreeHealthUiState.Loading

            try {
                val result = apiService.getTreeHealth("esp32_cam_1", manual)
                _uiState.value = TreeHealthUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = TreeHealthUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}