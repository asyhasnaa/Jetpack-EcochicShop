package com.dicoding.ecochic.di

import com.dicoding.ecochic.data.ProductRepository

object Injection {
    fun productRepository(): ProductRepository {
        return ProductRepository.getInstance()
    }
}