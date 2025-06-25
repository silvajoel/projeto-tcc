package com.joelchagas.tcc.ui.fragment.alimentacao

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.joelchagas.tcc.data.adapter.RefeicaoAdapter
import com.joelchagas.tcc.databinding.FragmentRefeicaoBinding

class Fragment_Refeicao : Fragment() {

    private var _binding: FragmentRefeicaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefeicaoViewModel by viewModels()
    private val adapter: RefeicaoAdapter = RefeicaoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRefeicaoBinding.inflate(inflater, container, false)

        binding.recyclerviewRefeicoes.layoutManager = LinearLayoutManager(context)

        // adapter
        binding.recyclerviewRefeicoes.adapter = adapter

        viewModel.getAllRefeicoes()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}