package com.joelchagas.tcc.ui.fragment.alimentacao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joelchagas.tcc.data.model.AlimentoConsumido
import com.joelchagas.tcc.data.model.TacoAlimento

class AlimentoViewModel : ViewModel() {

    private val _alimentosConsumidos = MutableLiveData<List<TacoAlimento>>()
    val alimentosConsumidos: LiveData<List<TacoAlimento>> get() = _alimentosConsumidos

    fun atualizarLista(novaLista: List<TacoAlimento>) {
        _alimentosConsumidos.value = novaLista
    }

    fun adicionar(alimento: TacoAlimento) {
        val atual = _alimentosConsumidos.value.orEmpty().toMutableList()
        atual.add(alimento)
        _alimentosConsumidos.value = atual
    }
}