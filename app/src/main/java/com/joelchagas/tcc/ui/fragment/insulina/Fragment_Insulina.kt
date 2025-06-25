package com.joelchagas.tcc.ui.fragment.insulina

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.adapter.InsulinaAdapter
import com.joelchagas.tcc.data.db.DatabaseHelper

class Fragment_Insulina : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InsulinaAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate o layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_insulina_, container, false)

        recyclerView = view.findViewById(R.id.recyclerInsulina)
        recyclerView.layoutManager = LinearLayoutManager(context)

        databaseHelper = DatabaseHelper(requireContext())
        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userID = sharedPreferences.getInt("USER_ID", 0)
        val listaInsulina = databaseHelper.getAllInsulinas(userID)
        adapter = InsulinaAdapter(listaInsulina)

        recyclerView.adapter = adapter

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fechar o databaseHelper se necessário, depende da implementação
        // databaseHelper.close()
    }
}
