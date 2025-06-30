package com.joelchagas.tcc.data.model

data class AlimentoConsumido(
    val id: Int = 0,
    val idRefeicao: Int,
    val nome: String,
    val quantidadeGramas: Double,
    val calorias: Double,
    val gorduras: Double,
    val carboidratos: Double
)