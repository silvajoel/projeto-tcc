package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.adapter.TACOAdapter
import com.joelchagas.tcc.data.model.TacoAlimento
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
        val tipoRefeicao = arguments?.getString("TIPO_REFEICAO") // ← capturando o valor recebido

        adapter = TACOAdapter(emptyList()) { selecionado ->
            val frag = DetalharAlimentoSelecionadoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("alimentoSelecionado", selecionado)
                    putString("TIPO_REFEICAO", tipoRefeicao) // ← repassando para a próxima tela
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
