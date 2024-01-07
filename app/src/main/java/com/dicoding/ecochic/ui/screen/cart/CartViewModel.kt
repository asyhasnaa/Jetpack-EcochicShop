package com.dicoding.ecochic.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecochic.data.ProductRepository
import com.dicoding.ecochic.model.Product
import com.dicoding.ecochic.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Product>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>>
        get() = _uiState

    fun getMarkProduct() = viewModelScope.launch {
        repository.getMarkProduct()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateProduct(id: Int, newState: Boolean) {
        repository.updateProduct(id, newState)
        getMarkProduct()
    }
}