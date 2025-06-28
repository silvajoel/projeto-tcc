package com.joelchagas.tcc.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TacoAlimento(
    val id: Int,
    val idUsuario: Int? = null,
    val descricao: String = "",
    val categoria: String = "",
    val calorias: Double? = null,
    val proteinas: Double? = null,
    val gorduras: Double? = null,
    val carboidratos: Double? = null
) : Parcelable