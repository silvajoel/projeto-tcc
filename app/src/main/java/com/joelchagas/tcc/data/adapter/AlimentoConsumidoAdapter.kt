package com.joelchagas.tcc.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.ItemAlimentoConsumidoBinding

class AlimentoConsumidoAdapter(
    private var itens: MutableList<TacoAlimento>,
    private val onItemClick: (TacoAlimento) -> Unit,
    private val onEditClick: (TacoAlimento) -> Unit,
    private val onDeleteClick: (TacoAlimento, Int) -> Unit
) : RecyclerView.Adapter<AlimentoConsumidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlimentoConsumidoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlimentoConsumidoBinding.inflate(inflater, parent, false)
        return AlimentoConsumidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlimentoConsumidoViewHolder, position: Int) {
        val item = itens[position]
        holder.bind(item)

        holder.itemView.setOnClickListener { onItemClick(item) }

        holder.binding.buttonEditar.setOnClickListener {
            try {
                onEditClick(item)
            } catch (e: Exception) {
                Log.e("Adapter", "Erro ao chamar onEditClick", e)
            }
        }

        holder.binding.buttonDelete.setOnClickListener {
            onDeleteClick(item, position)
        }
    }


    override fun getItemCount(): Int = itens.size

    fun removeItem(position: Int) {
        itens.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateList(novaLista: List<TacoAlimento>) {
        itens.clear()
        itens.addAll(novaLista)
        notifyDataSetChanged()
    }
}
