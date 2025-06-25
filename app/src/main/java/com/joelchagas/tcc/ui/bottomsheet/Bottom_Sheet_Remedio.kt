package com.joelchagas.tcc.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joelchagas.tcc.R

class Bottom_Sheet_Remedio : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.ButtomSheetDialog
    private val itens = arrayOf(
        "Em jejum (ao acordar)",
        "Antes das refeições",
        "2h após refeições",
        "Antes de dormir",
        "Outro (descreva a situação)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_remedio, container, false)

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, itens)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }

        return view
    }

}
