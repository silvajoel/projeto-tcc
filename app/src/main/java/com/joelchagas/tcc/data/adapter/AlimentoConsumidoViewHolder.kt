package com.joelchagas.tcc.data.adapter

import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.ItemAlimentoConsumidoBinding

class AlimentoConsumidoViewHolder(
    val binding: ItemAlimentoConsumidoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TacoAlimento) {
        binding.tvDescricao.text = item.descricao
        binding.tvQuantidade.text = "Quantidade: %.2f g".format(item.quantidade)
        binding.tvCalorias.text = "Calorias: %.2f kcal".format(item.calorias)
        binding.tvGorduras.text = "Gorduras: %.2f g".format(item.gorduras)
        binding.tvCarboidratos.text = "Carboidratos: %.2f g".format(item.carboidratos)
    }
}