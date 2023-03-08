package com.example.farmus_application.model

data class FarmDetail(
    val category: String,
    val description: String,
    val location: String,
    val name: String,
    val owner: String,
    val picture_url: String,
    val price: Int,
    val squaredMeters: Int,
    val tag: String,
    val term: String
)