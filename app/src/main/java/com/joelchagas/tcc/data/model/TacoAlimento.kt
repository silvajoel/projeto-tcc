package com.joelchagas.tcc.data.model

data class TacoAlimento(
    val id: Int,
    val description: String,
    val calories: Double?,
    val protein: Double?,
    val fat: Double?,
    val carbohydrate: Double?
)