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
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.data.model.TacoAlimento
import com.joelchagas.tcc.databinding.FragmentRefeicaoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class BuscarAlimentoTacoFragment : Fragment() {

    private var _binding: FragmentRefeicaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    private val viewModel: RefeicaoViewModel by viewModels()

    private lateinit var adapter: TACOAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRefeicaoBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        loadInitialData()
    }

    private fun setupRecyclerView() {
        adapter = TACOAdapter(mutableListOf()) { selecionado ->
            val bundle = Bundle().apply {
                putParcelable("alimentoSelecionado", selecionado)
            }

            val fragment = DetalharAlimentoSelecionadoFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerviewRefeicoes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@BuscarAlimentoTacoFragment.adapter
        }

        viewModel.alimentos.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filtrarAlimentos(newText.orEmpty())
                return true
            }
        })
    }

    private fun loadInitialData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val alimentos = loadJson()
                withContext(Dispatchers.Main) {
                    viewModel.todosAlimentos = alimentos
                    viewModel.filtrarAlimentos("")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadJson(): List<TacoAlimento> = withContext(Dispatchers.IO) {
        val jsonString = requireContext().assets
            .open("taco.json")
            .bufferedReader()
            .use { it.readText() }

        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }.decodeFromString(jsonString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
