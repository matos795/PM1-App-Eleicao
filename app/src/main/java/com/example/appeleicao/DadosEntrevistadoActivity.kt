package com.example.appeleicao

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DadosEntrevistadoActivity : AppCompatActivity() {

    private lateinit var etNomeEntrevistado: EditText
    private lateinit var etCelularEntrevistado: EditText
    private lateinit var btnConfirmarDados: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados_entrevistado)

        etNomeEntrevistado = findViewById(
            R.id.etNomeEntrevistado
        )

        etCelularEntrevistado = findViewById(
            R.id.etCelularEntrevistado
        )

        btnConfirmarDados = findViewById(
            R.id.btnConfirmarDados
        )

        btnVoltar = findViewById(R.id.btnVoltar)

        if (savedInstanceState == null) {
            etNomeEntrevistado.setText(PesquisaAtual.nome)
            etCelularEntrevistado.setText(PesquisaAtual.celular)
        }

        btnConfirmarDados.setOnClickListener {
            confirmarDados()
        }

        btnVoltar.setOnClickListener {
            finish()
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

    private fun confirmarDados() {
        etNomeEntrevistado.error = null
        etCelularEntrevistado.error = null

        val nome = etNomeEntrevistado.text.toString().trim()
        val celular = etCelularEntrevistado.text.toString().trim()

        if (nome.isEmpty()) {
            etNomeEntrevistado.error = getString(
                R.string.dados_nome_obrigatorio
            )

            etNomeEntrevistado.requestFocus()
            return
        }

        val formatoCelular = Regex("[1-9][0-9]9[0-9]{8}")

        if (!formatoCelular.matches(celular)) {
            etCelularEntrevistado.error = getString(
                R.string.dados_celular_invalido
            )

            etCelularEntrevistado.requestFocus()
            return
        }

        PesquisaAtual.nome = nome
        PesquisaAtual.celular = celular

        Toast.makeText(
            this,
            R.string.dados_confirmados,
            Toast.LENGTH_LONG
        ).show()

        voltarAoMenu()
    }

    private fun voltarAoMenu() {
        val telaMenu = Intent(
            this,
            MenuEntrevistadorActivity::class.java
        )

        telaMenu.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

        startActivity(telaMenu)
    }
}