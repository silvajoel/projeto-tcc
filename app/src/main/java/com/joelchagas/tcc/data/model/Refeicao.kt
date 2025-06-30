package com.joelchagas.tcc.data.model

data class Refeicao(
    val id: Int = 0,
    val idUsuario: Int,
    val tipo: TipoRefeicao,
    val dataHora: String
)

enum class TipoRefeicao {
    CAFE_DA_MANHA,
    ALMOCO,
    CAFE_DA_TARDE,
    JANTAR,
    CEIA,
    REFEICAO_AVULSA
}