package com.joelchagas.tcc.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.model.GlicemiaData

class GlicemiaAdapter(
    private val lista: List<GlicemiaData>) :
    RecyclerView.Adapter<GlicemiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nivelGlicose: TextView = view.findViewById(R.id.text_nivel_glicose)
        val dataHora: TextView = view.findViewById(R.id.text_data_hora)
        val momentoMedicao: TextView = view.findViewById(R.id.text_momento_medicao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_glicemia, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val glicemia = lista[position]

        val hora: String = if (glicemia.data_hora_medicao.length >= 16) {
            glicemia.data_hora_medicao.substring(11, 16)
        } else {
            "--:--"
        }

        with(holder) {
            nivelGlicose.text = "${glicemia.nivel_glicose} mg/dL"
            dataHora.text = "Registrado em ${glicemia.data_hora_medicao} às $hora"
            momentoMedicao.text = glicemia.momento_medicao
        }
    }

    override fun getItemCount(): Int = lista.size
}
