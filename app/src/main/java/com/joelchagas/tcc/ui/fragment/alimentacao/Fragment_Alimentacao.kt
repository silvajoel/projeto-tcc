package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.ui.fragment.relatorios.Fragment_Relatorios
import com.joelchagas.tcc.ui.fragment.remedios.Fragment_Remedios
import com.joelchagas.tcc.ui.fragment.glicemia.Fragment_Glicemia
import com.joelchagas.tcc.ui.fragment.insulina.Fragment_Insulina
import com.joelchagas.tcc.data.model.HomeCardItem
import com.joelchagas.tcc.data.model.TacoAlimento

class Fragment_Alimentacao : Fragment() {

    private val cards by lazy {
        listOf(
            HomeCardItem(R.drawable.cafe, "Café da Manhã", R.id.cardCafeManha, Fragment_Remedios()),
            HomeCardItem(R.drawable.almoco, "Almoço", R.id.cardAlmoco, Fragment_Insulina()),
            HomeCardItem(R.drawable.biscoito, "Café da tarde", R.id.cardCafeTarde, Fragment_Remedios()),
            HomeCardItem(R.drawable.alimentacao, "Jantar", R.id.cardJantar, Fragment_Remedios()),
            HomeCardItem(R.drawable.ultima_ceia, "Ceia", R.id.cardCeia, Fragment_Relatorios()),
            HomeCardItem(R.drawable.elipse, "Refeição Avulsa", R.id.cardAvulso, Fragment_Glicemia())
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
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, item.fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }
}
