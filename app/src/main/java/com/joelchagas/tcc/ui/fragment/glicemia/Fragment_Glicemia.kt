package com.joelchagas.tcc.ui.fragment.glicemia

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.adapter.GlicemiaAdapter
import com.joelchagas.tcc.data.db.DatabaseHelper

class Fragment_Glicemia : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GlicemiaAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_glicemia_, container, false)

        recyclerView = view.findViewById(R.id.recyclerGlicemia)
        recyclerView.layoutManager = LinearLayoutManager(context)

        databaseHelper = DatabaseHelper(requireContext())
        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userID = sharedPreferences.getInt("USER_ID", 0)
        val listaGlicemia = databaseHelper.getAllGlicemias(userID)
        adapter = GlicemiaAdapter(listaGlicemia)

        recyclerView.adapter = adapter

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
