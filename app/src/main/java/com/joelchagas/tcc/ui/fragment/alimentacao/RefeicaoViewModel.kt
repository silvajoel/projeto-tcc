package com.joelchagas.tcc.ui.fragment.alimentacao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joelchagas.tcc.data.model.TacoAlimento

class RefeicaoViewModel : ViewModel() {

    private val _alimentos = MutableLiveData<List<TacoAlimento>>()
    val alimentos: LiveData<List<TacoAlimento>> get() = _alimentos

    var todosAlimentos: List<TacoAlimento> = emptyList()

    fun filtrarAlimentos(query: String) {
        val resultado = if (query.isBlank()) {
            emptyList() // Não mostra nada até que seja realizada uma pesquisa
        } else {
            todosAlimentos.filter {
                it.descricao.contains(query, ignoreCase = true)
            }
        }

        _alimentos.value = resultado
    }
}
