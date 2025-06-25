package com.joelchagas.tcc.data.model

data class GlicemiaData(
    val id: Int,
    val userId: Int,
    val nivel_glicose: Int,
    val data_hora_medicao: String,
    val observacoes: String,
    val momento_medicao: String
)
