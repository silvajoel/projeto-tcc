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

class Bottom_Sheet_Glicemia : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.ButtomSheetDialog

    private val itens = arrayOf(
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
        val view = inflater.inflate(R.layout.bottomsheet_glicemia, container, false)

        // Inicializar o DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)
        val editNivelGlicemia = view.findViewById<TextInputEditText>(R.id.edit_glicemia)
        val editDataHoraMedicao = view.findViewById<TextInputEditText>(R.id.edit_data_hora_medicao)
        val editObservacoes = view.findViewById<TextInputEditText>(R.id.edit_observacoes)
        val buttonSave = view.findViewById<AppCompatButton>(R.id.buttonSave)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, itens)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }

        // Aplica o DatePicker e TimePicker ao campo
        setupDataHoraPicker(editDataHoraMedicao)

        buttonSave.setOnClickListener {
            val nivelGlicemia = editNivelGlicemia.text.toString()
            val dataHoraMedicao = editDataHoraMedicao.text.toString()
            val momentoMedicao = autoCompleteTextView.text.toString()
            val observacoes = editObservacoes.text.toString()

            insertGlicemia(nivelGlicemia, dataHoraMedicao, momentoMedicao, observacoes)
        }

        return view
    }

    private fun setupDataHoraPicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()

        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        editText.setText(dateTimeFormat.format(calendar.time)) // pré-preenche com data atual

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

    private fun insertGlicemia(
        nivelGlicemia: String,
        dataHoraMedicao: String,
        momentoMedicao: String,
        observacoes: String
    ) {
        when {
            nivelGlicemia.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe o nível de glicemia.", Toast.LENGTH_SHORT).show()
            }
            dataHoraMedicao.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe a data e hora da medição.", Toast.LENGTH_SHORT).show()
            }
            momentoMedicao.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe o momento da medição.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val rowId = databaseHelper.insertGlicemia(nivelGlicemia, dataHoraMedicao, momentoMedicao, observacoes)
                if (rowId != -1L) {
                    Toast.makeText(requireContext(), "Registro de glicemia salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar os dados!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
