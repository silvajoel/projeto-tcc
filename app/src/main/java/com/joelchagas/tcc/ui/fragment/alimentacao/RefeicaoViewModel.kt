package com.joelchagas.tcc.ui.fragment.alimentacao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RefeicaoViewModel: ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    //private val repository = RefeicaoR
    val text: LiveData<String> = _text

    fun getAllRefeicoes() {

    }
}