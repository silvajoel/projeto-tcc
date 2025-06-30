package com.joelchagas.tcc.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.ItemTacoAlimentoBinding

class TACOAdapter(
    private var itens: List<TacoAlimento>,
    private val onItemClick: (TacoAlimento) -> Unit
) : RecyclerView.Adapter<TACOAdapter.TACOViewHolder>() {

    inner class TACOViewHolder(
        private val binding: ItemTacoAlimentoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TacoAlimento) = with(binding) {
            tvDescricao.text = item.descricao

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TACOViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTacoAlimentoBinding.inflate(inflater, parent, false)
        return TACOViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TACOViewHolder, position: Int) {
        holder.bind(itens[position])
    }

    override fun getItemCount(): Int = itens.size

    fun updateList(novaLista: List<TacoAlimento>) {
        itens = novaLista
        notifyDataSetChanged()
    }
}
