package com.joelchagas.tcc.ui.fragment.home

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.joelchagas.tcc.R
import com.joelchagas.tcc.databinding.FragmentHomeBinding
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.ui.fragment.alimentacao.SelecionarRefeicaoFragment
import com.joelchagas.tcc.ui.fragment.glicemia.Fragment_Glicemia
import com.joelchagas.tcc.ui.fragment.insulina.Fragment_Insulina
import com.joelchagas.tcc.ui.fragment.relatorios.Fragment_Relatorios
import com.joelchagas.tcc.ui.fragment.remedios.Fragment_Remedios
import com.joelchagas.tcc.data.model.HomeCardItem
import com.joelchagas.tcc.ui.auth.HomeToMain

class Fragment_Home : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val cards by lazy {
        listOf(
            HomeCardItem(
                R.drawable.glicosimetro, "Glicemia",
                R.id.cardGlicemia, Fragment_Glicemia()
            ),
            HomeCardItem(R.drawable.insulina, "Insulina", R.id.cardInsulina, Fragment_Insulina()),
            HomeCardItem(
                R.drawable.alimentacao, "Alimentação",
                R.id.cardAlimentacao, SelecionarRefeicaoFragment()
            ),
            HomeCardItem(R.drawable.remedio, "Remédios", R.id.cardRemedio, Fragment_Remedios()),
            HomeCardItem(
                R.drawable.relatorio, "Relatórios",
                R.id.cardRelatorio, Fragment_Relatorios()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userNome = sharedPreferences.getString("NOME", "Usuário")
        val userID = sharedPreferences.getInt("USER_ID", 0)

        val glicemiaAtual = databaseHelper.getGlicemiaAtual(userID)
        binding.txtOla.text = "Olá, $userNome"

        val glicemiaInt = glicemiaAtual ?: 0
        binding.textValor.text = "$glicemiaInt mg/dL"

        val cardAlerta = view.findViewById<CardView>(R.id.card_alerta)
        if (glicemiaInt >= 140) {
            cardAlerta.visibility = View.VISIBLE
        } else {
            cardAlerta.visibility = View.GONE
        }

        // Configurar todos os cards dinamicamente
        cards.forEach { item ->
            val card = view.findViewById<CardView>(item.cardId)
            val icon = card.findViewById<ImageView>(R.id.card_icon)
            val label = card.findViewById<TextView>(R.id.card_label)

            icon.setImageResource(item.iconRes)
            label.text = item.label

            card.setOnClickListener {
                (activity as? HomeToMain)?.openFragment(item.fragment)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
