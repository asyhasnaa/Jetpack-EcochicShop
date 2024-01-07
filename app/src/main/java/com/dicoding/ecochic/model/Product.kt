package com.dicoding.ecochic.model

data class Product(
    val id: Int,
    val image: Int,
    val title: String,
    val product: String,
    val price: String,
    val description: String,
    val size: String,
    var isMark: Boolean = false

)
