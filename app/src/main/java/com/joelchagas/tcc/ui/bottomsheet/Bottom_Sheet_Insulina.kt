package com.joelchagas.tcc.ui.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.db.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class Bottom_Sheet_Insulina : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.ButtomSheetDialog

    private val tiposInsulina = arrayOf(
        "Rápida",
        "Ultrarrápida",
        "Intermediária",
        "Basal",
        "Combinada"
    )

    private val momentos = arrayOf(
        "Em jejum (ao acordar)",
        "Antes das refeições",
        "2h após refeições",
        "Antes de dormir",
        "Outro (descreva a situação)"
    )

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_insulina, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        val editQuantidade = view.findViewById<TextInputEditText>(R.id.edit_insulina_quantidade)
        val autoCompleteInsulina = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_tipo_insulina)
        val editDataHoraAplicacao = view.findViewById<TextInputEditText>(R.id.edit_data_hora_aplicacao)
        val editObservacoes = view.findViewById<TextInputEditText>(R.id.edit_observacoes)
        val autoCompleteMomento = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_momento)
        val buttonSave = view.findViewById<AppCompatButton>(R.id.buttonSave)

        // Adaptadores dos menus dropdown
        val adapterInsulina = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tiposInsulina)
        autoCompleteInsulina.setAdapter(adapterInsulina)
        autoCompleteInsulina.setOnClickListener { autoCompleteInsulina.showDropDown() }

        val adapterMomento = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, momentos)
        autoCompleteMomento.setAdapter(adapterMomento)
        autoCompleteMomento.setOnClickListener { autoCompleteMomento.showDropDown() }

        setupDataHoraPicker(editDataHoraAplicacao)

        buttonSave.setOnClickListener {
            val quantidade = editQuantidade.text.toString()
            val tipoInsulina = autoCompleteInsulina.text.toString()
            val dataHoraAplicacao = editDataHoraAplicacao.text.toString()
            val observacoes = editObservacoes.text.toString()
            val momento = autoCompleteMomento.text.toString()

            insertInsulina(quantidade, tipoInsulina, dataHoraAplicacao, momento, observacoes)
        }

        return view
    }

    private fun setupDataHoraPicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        editText.setText(dateTimeFormat.format(calendar.time))

        editText.setOnClickListener {
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    TimePickerDialog(requireContext(),
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            editText.setText(dateTimeFormat.format(calendar.time))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun insertInsulina(
        quantidade: String,
        tipoInsulina: String,
        dataHoraAplicacao: String,
        momento: String,
        observacoes: String
    ) {
        when {
            quantidade.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe a quantidade de insulina.", Toast.LENGTH_SHORT).show()
            }
            tipoInsulina.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe o tipo de insulina.", Toast.LENGTH_SHORT).show()
            }
            dataHoraAplicacao.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe a data e hora da aplicação.", Toast.LENGTH_SHORT).show()
            }
            momento.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe o momento da aplicação.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val rowId = databaseHelper.insertInsulina(quantidade, tipoInsulina, dataHoraAplicacao, momento, observacoes)
                if (rowId != -1L) {
                    Toast.makeText(requireContext(), "Registro de insulina salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar os dados!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
