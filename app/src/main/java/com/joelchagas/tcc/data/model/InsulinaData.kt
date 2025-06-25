package com.joelchagas.tcc.data.model

data class InsulinaData(
    val id: Int,
    val userId: Int,
    val quantidade: Int,
    val tipo: String,
    val dataHora: String,
    val observacoes: String,
    val momento: String
)
