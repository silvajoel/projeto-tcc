package com.joelchagas.tcc.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.ItemRefeicaoBinding

class RefeicaoAdapter : RecyclerView.Adapter<RefeicaoViewHolder>() {

    private var refeicaoList: List<TacoAlimento> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefeicaoViewHolder {
        val view = ItemRefeicaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RefeicaoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return refeicaoList.size
    }

    override fun onBindViewHolder(holder: RefeicaoViewHolder, position: Int) {
        holder.bind(refeicaoList[position])
    }

    fun updateRefeicoes(list: List<TacoAlimento>){

        refeicaoList = list

    }
}