package com.joelchagas.tcc.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.model.InsulinaData

public class InsulinaAdapter(private val lista: List<InsulinaData>) :
    RecyclerView.Adapter<InsulinaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tipo: TextView = view.findViewById(R.id.text_tipo_insulina)
        val momento: TextView = view.findViewById(R.id.text_momento)
        val quantidade: TextView = view.findViewById(R.id.text_quantidade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_insulina, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val insulina = lista[position]

        val hora: String = if (insulina.dataHora.length >= 16) {
            insulina.dataHora.substring(11, 16)
        } else {
            "" // ou um valor padrão, ex: "--:--"
        }

        with(holder) {
            tipo.text = insulina.tipo
            momento.text = "${insulina.momento} às $hora"
            quantidade.text = "${insulina.quantidade} UI"
        }
    }

    override fun getItemCount(): Int = lista.size
}
