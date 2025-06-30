package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.model.HomeCardItem
import java.text.Normalizer

class SelecionarRefeicaoFragment : Fragment() {

    private val cards by lazy {
        listOf(
            HomeCardItem(R.drawable.cafe, "Café da Manhã", R.id.cardCafeManha, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.almoco, "Almoço", R.id.cardAlmoco, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.biscoito, "Café da tarde", R.id.cardCafeTarde, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.alimentacao, "Jantar", R.id.cardJantar, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.ultima_ceia, "Ceia", R.id.cardCeia, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.elipse, "Refeição Avulsa", R.id.cardAvulso, ListaAlimentosRefeicaoFragment())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alimentacao_, container, false)

        cards.forEach { item ->
            val card = view.findViewById<CardView>(item.cardId)
            val icon = card.findViewById<ImageView>(R.id.card_icon)
            val label = card.findViewById<TextView>(R.id.card_label)

            icon.setImageResource(item.iconRes)
            label.text = item.label

            card.setOnClickListener {
                val tipoRefeicao = removerAcentos(item.label.uppercase()).replace(" ", "_")

                val bundle = Bundle().apply {
                    putString("TIPO_REFEICAO", tipoRefeicao)
                }

                val fragment = item.fragment
                fragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    fun removerAcentos(texto: String): String {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }
}
