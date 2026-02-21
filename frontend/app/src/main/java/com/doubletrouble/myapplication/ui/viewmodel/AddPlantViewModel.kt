package com.doubletrouble.myapplication.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletrouble.myapplication.data.MyApiService
import com.doubletrouble.myapplication.data.model.Tree
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TreeUiState {
    object Idle : TreeUiState()
    object Loading : TreeUiState()
    data class Success(val tree: Tree) : TreeUiState()
    data class Error(val message: String) : TreeUiState()
}

class AddPlantViewModel(private val apiService: MyApiService) : ViewModel() {

    private val _uiState = MutableStateFlow<TreeUiState>(TreeUiState.Idle)
    val uiState: StateFlow<TreeUiState> = _uiState.asStateFlow()

    fun fetchTree() {
        viewModelScope.launch {
            _uiState.value = TreeUiState.Loading

            try {
                val result = apiService.getTree("esp32_cam_1")
                _uiState.value = TreeUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = TreeUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}