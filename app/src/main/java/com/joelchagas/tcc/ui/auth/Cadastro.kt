package com.joelchagas.tcc.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.joelchagas.tcc.R
import com.joelchagas.tcc.ui.auth.LoginActivity
import com.joelchagas.tcc.databinding.ActivityCadastroBinding
import com.joelchagas.tcc.data.db.DatabaseHelper

class Cadastro : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonEntrar2.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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

    @SuppressLint("SuspiciousIndentation")
    private fun sigupDatabase(nome: String, sobrenome: String, dataNasc: String, email: String, senha: String) {

        when {
            nome.isEmpty() -> {
                Toast.makeText(this, "Digite o nome.", Toast.LENGTH_SHORT).show()
                binding.editNome.requestFocus()
            }

            sobrenome.isEmpty() -> {
                Toast.makeText(this, "Digite o sobrenome.", Toast.LENGTH_SHORT).show()
                binding.editSobrenome.requestFocus()
            }

            dataNasc.isEmpty() -> {
                Toast.makeText(this, "Digite a data de nascimento.", Toast.LENGTH_SHORT).show()
                binding.editDataNascimento.requestFocus()
            }

            email.isEmpty() -> {
                Toast.makeText(this, "Digite o E-mail.", Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
            }

            senha.isEmpty() -> {
                Toast.makeText(this, "Digite a senha.", Toast.LENGTH_SHORT).show()
                binding.editSenha.requestFocus()
            }
            else -> {
                val insertRowId = databaseHelper.insertUsuario(nome, sobrenome, dataNasc, email, senha)
                try {
                    if (insertRowId != -1L)
                        Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                } catch (ex: Exception) {
                    Toast.makeText(this, "Erro ao inserir: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun onClick(view: View) {}
    /*
    private fun clearFields() {
        binding.editNome.text?.clear()
        binding.editSobrenome.text?.clear()
        binding.editDataNascimento.text?.clear()
        binding.editEmail.text?.clear()
        binding.editSenha.text?.clear()
    }*/

}
