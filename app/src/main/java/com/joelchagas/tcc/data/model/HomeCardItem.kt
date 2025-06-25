package com.joelchagas.tcc.data.model

import androidx.fragment.app.Fragment

data class HomeCardItem(
    val iconRes: Int,
    val label: String,
    val cardId: Int,
    val fragment: Fragment
)
