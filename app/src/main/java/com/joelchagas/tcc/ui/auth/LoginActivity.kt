package com.joelchagas.tcc.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.joelchagas.tcc.databinding.ActivityLoginBinding
import com.joelchagas.tcc.data.db.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        databaseHelper = DatabaseHelper(this)

        binding.buttonLogin.setOnClickListener { view ->
            val email = binding.editEmail.text.toString().trim()
            val senha = binding.editPassword.text.toString()

            when {
                email.isEmpty() -> binding.editEmail.error = "Preencha o E-mail"
                senha.isEmpty() -> binding.editPassword.error = "Preencha a Senha"
                !email.contains("@") -> Snackbar.make(view, "E-mail inválido", Snackbar.LENGTH_SHORT).show()
                senha.length < 8 -> Snackbar.make(view, "A senha precisa ter pelo menos 8 caracteres!", Snackbar.LENGTH_SHORT).show()
                else -> loginDatabase(email, senha, view)
            }
        }

        binding.buttonCadastreSe.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }
    }

    private fun loginDatabase(email: String, senha: String, view: View) {
        val userId = databaseHelper.getUsuarioId(email, senha)

        if (userId != null) {
            val userNome = databaseHelper.getUsuarioNome(email)

            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            sharedPreferences.edit()
                .putString("EMAIL", email)
                .putString("NOME", userNome)
                .putInt("USER_ID", userId)
                .apply()

            binding.progressBar.visibility = View.VISIBLE
            Snackbar.make(view, "Login efetuado com sucesso!", Snackbar.LENGTH_SHORT).show()

            view.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1500)

        } else {
            Snackbar.make(view, "Falha ao realizar o login!", Snackbar.LENGTH_SHORT).show()
        }
    }
}
