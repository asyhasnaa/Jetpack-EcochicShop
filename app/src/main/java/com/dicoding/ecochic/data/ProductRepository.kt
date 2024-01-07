package com.dicoding.ecochic.data

import com.dicoding.ecochic.model.Product
import com.dicoding.ecochic.model.ProductData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class     ProductRepository {

    private val dummyProduct = mutableListOf<Product>()

    init {
        if (dummyProduct.isEmpty()) {
            ProductData.dummyProduct.forEach {
                dummyProduct.add(it)
            }
        }
    }

    fun getProductById(productId: Int): Product {
        return dummyProduct.first {
            it.id == productId
        }

    }

    fun searchProduct(query: String) = flow {
        val result = mutableListOf<Product>()
        dummyProduct.filterTo(result) {
            it.product.contains(query, ignoreCase = true)
        }
        emit(result)
    }

    fun getMarkProduct() : Flow<List<Product>> {
        return flowOf(dummyProduct.filter { it.isMark })
    }


    fun updateProduct(productId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummyProduct.indexOfFirst { it.id == productId }
        val result = if (index >= 0) {
            val hsrCharacter = dummyProduct[index]
            dummyProduct[index] = hsrCharacter.copy(isMark = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }


    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(): ProductRepository =
            instance ?: synchronized(this) {
                ProductRepository().apply {
                    instance = this
                }
            }
    }
}