package com.joelchagas.tcc.data.adapter

import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.ItemTacoAlimentoBinding

class TACOViewHolder(
    private val binding: ItemTacoAlimentoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(refeicao: TacoAlimento) {
        binding.tvDescricao.text = refeicao.descricao
    }
}
