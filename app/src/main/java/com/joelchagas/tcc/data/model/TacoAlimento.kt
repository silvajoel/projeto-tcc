package com.joelchagas.tcc.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TacoAlimento(
    val id: Int,
    val idUsuario: Int? = null,
    val descricao: String = "",
    val categoria: String = "",
    val quantidade: Double = 0.0,
    var calorias: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val carboidratos: Double = 0.0
) : Parcelable