package com.example.appeleicao

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etSenha: EditText
    private lateinit var tvErro: TextView
    private lateinit var btnAcessar: Button
    private lateinit var btnFinalizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.etUsuario)
        etSenha = findViewById(R.id.etSenha)
        tvErro = findViewById(R.id.tvErro)
        btnAcessar = findViewById(R.id.btnAcessar)
        btnFinalizar = findViewById(R.id.btnFinalizar)

        btnAcessar.setOnClickListener {
            logar()
        }

        btnFinalizar.setOnClickListener {
            finalizar()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                barras.left,
                barras.top,
                barras.right,
                barras.bottom
            )

            insets
        }
    }

    private fun logar() {
        tvErro.visibility = View.GONE
        etUsuario.error = null
        etSenha.error = null

        val usuario = etUsuario.text.toString().trim()
        val senha = etSenha.text.toString()

        if (usuario.isEmpty()) {
            etUsuario.error = getString(
                R.string.login_usuario_obrigatorio
            )
            etUsuario.requestFocus()
            return
        }

        if (senha.isEmpty()) {
            etSenha.error = getString(
                R.string.login_senha_obrigatoria
            )
            etSenha.requestFocus()
            return
        }

        if (usuario == "admin" && senha == "admin") {
            Toast.makeText(
                this,
                R.string.login_admin_sucesso,
                Toast.LENGTH_SHORT
            ).show()

        } else if (
            usuario == "entrevistador" &&
            senha == "entrevistador"
        ) {
            Toast.makeText(
                this,
                R.string.login_entrevistador_sucesso,
                Toast.LENGTH_SHORT
            ).show()

        } else {
            tvErro.text = getString(R.string.login_erro)
            tvErro.visibility = View.VISIBLE
        }
    }

    private fun finalizar() {
        finishAndRemoveTask()
    }
}