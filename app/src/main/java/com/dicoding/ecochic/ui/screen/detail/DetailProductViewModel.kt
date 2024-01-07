package com.dicoding.ecochic.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecochic.data.ProductRepository
import com.dicoding.ecochic.model.Product
import com.dicoding.ecochic.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Product>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Product>>
        get() = _uiState

    private fun setLoadingState() {
        _uiState.value = UiState.Loading
    }

    fun getProductById(id: Int) = viewModelScope.launch {
        setLoadingState()
        _uiState.value = UiState.Success(repository.getProductById(id))
    }

    fun updateProduct(id: Int, newState: Boolean) = viewModelScope.launch {
        setLoadingState()
        repository.updateProduct(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getProductById(id)
            }
    }
}