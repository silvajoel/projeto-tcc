package com.joelchagas.tcc.ui.fragment.alimentacao

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.adapter.AlimentoConsumidoAdapter
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.DialogEditarAlimentoBinding
import com.joelchagas.tcc.databinding.FragmentDetalheRefeicaoBinding

class ListaAlimentosRefeicaoFragment : Fragment() {

    private var _binding: FragmentDetalheRefeicaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlimentoViewModel by viewModels()
    private lateinit var adapter: AlimentoConsumidoAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalheRefeicaoBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userID = sharedPreferences.getInt("USER_ID", 0)
        val tipoRefeicao = arguments?.getString("TIPO_REFEICAO")

        if (tipoRefeicao.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Tipo de refeição não informado", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val listaAlimentos = databaseHelper.getAllAlimentos(userID, tipoRefeicao)

            adapter = AlimentoConsumidoAdapter(
                itens = listaAlimentos.toMutableList(),
                onItemClick = { /* ação opcional */ },
                onEditClick = { alimento -> abrirTelaEdicao(alimento) },
                onDeleteClick = { alimento, _ ->
                    databaseHelper.deleteAlimento(alimento.id)
                    adapter.updateList(databaseHelper.getAllAlimentos(userID, tipoRefeicao))
                    binding.textViewEmptyList.visibility =
                        if (adapter.itemCount == 0) View.VISIBLE else View.GONE
                }
            )

            binding.recyclerviewRefeicoes.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = this@ListaAlimentosRefeicaoFragment.adapter
            }

            binding.textViewEmptyList.visibility =
                if (listaAlimentos.isEmpty()) View.VISIBLE else View.GONE

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao carregar alimentos: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        binding.fab.setOnClickListener {
            val buscarAlimentoFragment = BuscarAlimentoTacoFragment().apply {
                arguments = Bundle().apply {
                    putString("TIPO_REFEICAO", tipoRefeicao)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, buscarAlimentoFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun abrirTelaEdicao(alimento: TacoAlimento) {
        val dialogBinding =
            DialogEditarAlimentoBinding.inflate(layoutInflater)

        dialogBinding.apply {
            tvDescricao.text = alimento.descricao.toString()
            etQuantidade.setText(alimento.quantidade.toString())
            etCalorias.setText(alimento.calorias.toString())
            etGorduras.setText(alimento.gorduras.toString())
            etCarboidratos.setText(alimento.carboidratos.toString())
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar alimento")
            .setView(dialogBinding.root)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Salvar") { _, _ ->
                salvarEdicao(alimento, dialogBinding)
            }
            .create()

        dialog.show()
    }

    private fun salvarEdicao(
        original: TacoAlimento,
        binding: DialogEditarAlimentoBinding
    ) {
        try {
            val editado = original.copy(
                quantidade   = binding.etQuantidade.text.toString().toDoubleOrNull() ?: 0.0,
                calorias     = binding.etCalorias.text.toString().toDoubleOrNull() ?: 0.0,
                gorduras     = binding.etGorduras.text.toString().toDoubleOrNull() ?: 0.0,
                carboidratos = binding.etCarboidratos.text.toString().toDoubleOrNull() ?: 0.0
            )

            val linhas = databaseHelper.updateAlimento(editado)
            if (linhas > 0) {
                val prefs        = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
                val userId       = prefs.getInt("USER_ID", 0)
                val tipoRefeicao = arguments?.getString("TIPO_REFEICAO") ?: ""
                adapter.updateList(
                    databaseHelper.getAllAlimentos(userId, tipoRefeicao)
                )
                Toast.makeText(requireContext(), "Alimento atualizado!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Nenhuma alteração registrada.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("EditarAlimento", "Erro ao salvar edição", e)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}