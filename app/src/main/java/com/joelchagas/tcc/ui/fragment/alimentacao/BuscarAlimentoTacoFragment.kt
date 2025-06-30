package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.adapter.TACOAdapter
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.DialogAdicionarAlimentoBinding
import com.joelchagas.tcc.databinding.DialogEditarAlimentoBinding
import com.joelchagas.tcc.databinding.FragmentBuscarAlimentoTacoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class BuscarAlimentoTacoFragment : Fragment() {

    private var _binding: FragmentBuscarAlimentoTacoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefeicaoViewModel by viewModels()
    private lateinit var adapter: TACOAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarAlimentoTacoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        loadInitialData()
    }

    private fun setupRecyclerView() {
        val tipoRefeicao = arguments?.getString("TIPO_REFEICAO")

        adapter = TACOAdapter(emptyList()) { selecionado ->
            dialogAdicionarAlimento(selecionado) // Apenas exibe o dialog
        }

        binding.recyclerviewRefeicoes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            this.adapter = this@BuscarAlimentoTacoFragment.adapter
        }
    }


    private fun setupSearchView() {
        binding.searchView.apply {
            isIconified = false
            queryHint = "Procure um alimento…"

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.filtrarAlimentos(newText.orEmpty())
                    return true
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.alimentos.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)
        }
    }

    private fun loadInitialData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val alimentos = loadJsonFromAssets()
            withContext(Dispatchers.Main) {
                viewModel.todosAlimentos = alimentos
                viewModel.filtrarAlimentos("")
            }
        }
    }

    private suspend fun loadJsonFromAssets(): List<TacoAlimento> = withContext(Dispatchers.IO) {
        val jsonString = requireContext().assets
            .open("taco.json")
            .bufferedReader()
            .use { it.readText() }

        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
            .decodeFromString(jsonString)
    }

    private fun dialogAdicionarAlimento(alimento: TacoAlimento) {
        val dialogBinding = DialogAdicionarAlimentoBinding.inflate(layoutInflater)

        dialogBinding.tvDescricao.text = alimento.descricao
        dialogBinding.etQuantidade.setText("")

        val tipoRefeicao = arguments?.getString("TIPO_REFEICAO") ?: "Indefinido"

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Adicionar alimento")
            .setView(dialogBinding.root)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Adicionar") { _, _ ->
                val quantidade = dialogBinding.etQuantidade.text.toString().toDoubleOrNull()

                if (quantidade != null && quantidade > 0) {
                    // Calcular valores proporcionalmente
                    val fator = quantidade / 100

                    val calorias = alimento.calorias * fator
                    val proteinas = alimento.proteinas * fator
                    val gorduras = alimento.gorduras * fator
                    val carboidratos = alimento.carboidratos * fator

                    val db = DatabaseHelper(requireContext())

                    val dataHoraAtual =
                        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())
                    val idRefeicao = db.insertRefeicao(tipoRefeicao, dataHoraAtual).toInt()

                    db.insertAlimento(
                        descricao = alimento.descricao,
                        categoria = alimento.categoria,
                        quantidade_gramas = quantidade,
                        calorias = calorias,
                        proteinas = proteinas,
                        gorduras = gorduras,
                        carboidratos = carboidratos,
                        idRefeicao = idRefeicao
                    )

                    Toast.makeText(
                        requireContext(),
                        "Alimento adicionado, retornando à tela inicial",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
