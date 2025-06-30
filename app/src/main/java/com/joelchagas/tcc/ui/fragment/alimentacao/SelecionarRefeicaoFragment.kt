package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.model.HomeCardItem
import com.joelchagas.tcc.databinding.FragmentSelecionarRefeicaoBinding
import java.text.Normalizer

class SelecionarRefeicaoFragment : Fragment() {

    private var _binding: FragmentSelecionarRefeicaoBinding? = null
    private val binding get() = _binding!!

    private val cards by lazy {
        listOf(
            HomeCardItem(R.drawable.cafe,        "Café da Manhã",  R.id.cardCafeManha, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.almoco,      "Almoço",          R.id.cardAlmoco,    ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.biscoito,    "Café da Tarde",   R.id.cardCafeTarde, ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.alimentacao, "Jantar",          R.id.cardJantar,    ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.ultima_ceia, "Ceia",            R.id.cardCeia,      ListaAlimentosRefeicaoFragment()),
            HomeCardItem(R.drawable.elipse,      "Refeição Avulsa", R.id.cardAvulso,    ListaAlimentosRefeicaoFragment())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelecionarRefeicaoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cards.forEach { item ->
            val cardView = binding.root.findViewById<MaterialCardView>(item.cardId)

            val iconResId = when (item.cardId) {
                R.id.cardCafeManha -> R.id.iconCafeManha
                R.id.cardAlmoco    -> R.id.iconAlmoco
                R.id.cardCafeTarde -> R.id.iconCafeTarde
                R.id.cardJantar    -> R.id.iconJantar
                R.id.cardCeia      -> R.id.iconCeia
                R.id.cardAvulso    -> R.id.iconAvulso
                else               -> 0
            }

            val labelResId = when (item.cardId) {
                R.id.cardCafeManha -> R.id.labelCafeManha
                R.id.cardAlmoco    -> R.id.labelAlmoco
                R.id.cardCafeTarde -> R.id.labelCafeTarde
                R.id.cardJantar    -> R.id.labelJantar
                R.id.cardCeia      -> R.id.labelCeia
                R.id.cardAvulso    -> R.id.labelAvulso
                else               -> 0
            }

            val iconView = cardView.findViewById<ImageView>(iconResId)
            val labelView = cardView.findViewById<TextView>(labelResId)

            iconView.setImageResource(item.iconRes)
            labelView.text = item.label

            cardView.setOnClickListener {
                val tipo = removerAcentos(item.label.uppercase()).replace(" ", "_")
                val bundle = Bundle().apply {
                    putString("TIPO_REFEICAO", tipo)
                }
                findNavController().navigate(
                    R.id.action_selecionar_to_lista,
                    bundle
                )
            }
        }
    }

    private fun removerAcentos(texto: String): String =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
