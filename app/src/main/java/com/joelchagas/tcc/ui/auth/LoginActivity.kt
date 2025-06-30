package com.joelchagas.tcc.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.joelchagas.tcc.R
import com.joelchagas.tcc.data.db.DatabaseHelper
import com.joelchagas.tcc.databinding.ActivityLoginBinding

class LoginActivity : Fragment() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(requireContext())

        binding.buttonLogin.setOnClickListener { clickedView ->
            val email = binding.editEmail.text.toString().trim()
            val senha = binding.editPassword.text.toString()

            when {
                email.isEmpty() -> binding.editEmail.error = "Preencha o E-mail"
                senha.isEmpty() -> binding.editPassword.error = "Preencha a Senha"
                !email.contains("@") -> Snackbar.make(clickedView, "E-mail inválido", Snackbar.LENGTH_SHORT).show()
                senha.length < 8 -> Snackbar.make(clickedView, "A senha precisa ter pelo menos 8 caracteres!", Snackbar.LENGTH_SHORT).show()
                else -> loginDatabase(email, senha, clickedView)
            }
        }

        binding.buttonCadastreSe.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_cadastro)
        }
    }

    private fun loginDatabase(email: String, senha: String, view: View) {
        val userId = databaseHelper.getUsuarioId(email, senha)

        if (userId != null) {
            val userNome = databaseHelper.getUsuarioNome(email)

            val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putString("EMAIL", email)
                .putString("NOME", userNome)
                .putInt("USER_ID", userId)
                .apply()

            //binding.progressBar.visibility = View.VISIBLE

            Snackbar.make(view, "Login efetuado com sucesso!", Snackbar.LENGTH_SHORT).show()

            view.postDelayed({
                findNavController().navigate(R.id.action_login_to_home)
            }, 1500)
        } else {
            Snackbar.make(view, "Falha ao realizar o login!", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
