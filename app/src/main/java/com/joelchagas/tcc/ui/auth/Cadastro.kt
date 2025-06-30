package com.joelchagas.tcc.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.databinding.ActivityCadastroBinding

class Cadastro : Fragment() {

    private var _binding: ActivityCadastroBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(requireContext())

        binding.buttonEntrar2.setOnClickListener {
            findNavController().navigate(R.id.action_cadastro_to_login)
        }

        binding.buttonCadastrar.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val sobrenome = binding.editSobrenome.text.toString()
            val dataNasc = binding.editDataNascimento.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            sigupDatabase(nome, sobrenome, dataNasc, email, senha)
        }
    }

    private fun sigupDatabase(
        nome: String,
        sobrenome: String,
        dataNasc: String,
        email: String,
        senha: String
    ) {
        when {
            nome.isEmpty() -> {
                Toast.makeText(requireContext(), "Digite o nome.", Toast.LENGTH_SHORT).show()
                binding.editNome.requestFocus()
            }

            sobrenome.isEmpty() -> {
                Toast.makeText(requireContext(), "Digite o sobrenome.", Toast.LENGTH_SHORT).show()
                binding.editSobrenome.requestFocus()
            }

            dataNasc.isEmpty() -> {
                Toast.makeText(requireContext(), "Digite a data de nascimento.", Toast.LENGTH_SHORT).show()
                binding.editDataNascimento.requestFocus()
            }

            email.isEmpty() -> {
                Toast.makeText(requireContext(), "Digite o E-mail.", Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
            }

            senha.isEmpty() -> {
                Toast.makeText(requireContext(), "Digite a senha.", Toast.LENGTH_SHORT).show()
                binding.editSenha.requestFocus()
            }

            else -> {
                val insertRowId = databaseHelper.insertUsuario(nome, sobrenome, dataNasc, email, senha)
                try {
                    if (insertRowId != -1L) {
                        Toast.makeText(requireContext(), "Dados inseridos com sucesso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_cadastro_to_login)
                    }
                } catch (ex: Exception) {
                    Toast.makeText(requireContext(), "Erro ao inserir: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
