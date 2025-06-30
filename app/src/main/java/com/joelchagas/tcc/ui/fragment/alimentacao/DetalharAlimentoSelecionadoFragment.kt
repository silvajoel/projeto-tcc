package com.joelchagas.tcc.ui.fragment.alimentacao

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.data.model.TipoRefeicao
import com.joelchagas.tcc.databinding.AdicionarAlimentoFragmentBinding

@Suppress("DEPRECATION")
class DetalharAlimentoSelecionadoFragment : Fragment() {

    private var _binding: AdicionarAlimentoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var alimento: TacoAlimento
    private var tipoRefeicao: TipoRefeicao? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        databaseHelper = DatabaseHelper(requireContext())
        _binding = AdicionarAlimentoFragmentBinding.inflate(inflater, container, false)

        val tipoRefeicaoString = arguments?.getString("TIPO_REFEICAO")
        tipoRefeicao = try {
            tipoRefeicaoString?.let { TipoRefeicao.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e("DetalharAlimento", "TipoRefeicao inválido: $tipoRefeicaoString")
            null
        }

        Log.d("DetalharAlimento", "Tipo refeição recebida: $tipoRefeicao")

        val alimentoSelecionado = arguments?.getParcelable<TacoAlimento>("alimentoSelecionado")

        if (alimentoSelecionado != null) {
            alimento = alimentoSelecionado
            binding.txtNomeAlimento.text = alimento.descricao
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.buttonSave.setOnClickListener {
            val quantidade = binding.editQuantidadeAlimento.text.toString().toDoubleOrNull()

            if (quantidade != null && tipoRefeicao != null) {
                insertAlimento(quantidade, tipoRefeicao!!)
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    context,
                    "Erro: tipo da refeição ou quantidade inválida.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "Alimento: ${alimento.descricao}", Toast.LENGTH_SHORT)
            .show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun insertAlimento(quantidade: Double, tipoRefeicao: TipoRefeicao) {
        val dataHoraAtual =
            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())
        val idRefeicao = databaseHelper.insertRefeicao(tipoRefeicao.toString(), dataHoraAtual)

        if (idRefeicao == -1L) {
            Toast.makeText(requireContext(), "Erro ao salvar a refeição!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val fator = quantidade / 100.0

        val calorias = alimento.calorias * fator
        val proteinas = alimento.proteinas * fator
        val gorduras = alimento.gorduras * fator
        val carboidratos = alimento.carboidratos * fator

        val rowId = databaseHelper.insertAlimento(
            descricao = alimento.descricao,
            categoria = alimento.categoria,
            quantidade_gramas = quantidade,
            calorias = calorias,
            proteinas = proteinas,
            gorduras = gorduras,
            carboidratos = carboidratos,
            idRefeicao = idRefeicao.toInt()
        )

        if (rowId != -1L) {
            Toast.makeText(requireContext(), "Alimento salvo com sucesso!", Toast.LENGTH_SHORT)
                .show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Erro ao salvar o alimento!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
