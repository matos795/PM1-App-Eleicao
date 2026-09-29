package com.example.appeleicao

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaEspontaneaActivity : AppCompatActivity() {

    private lateinit var etRespostaEspontanea: EditText
    private lateinit var btnConfirmarEspontanea: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_espontanea)

        etRespostaEspontanea = findViewById(
            R.id.etRespostaEspontanea
        )

        btnConfirmarEspontanea = findViewById(
            R.id.btnConfirmarEspontanea
        )

        btnVoltar = findViewById(R.id.btnVoltar)

        if (savedInstanceState == null) {
            etRespostaEspontanea.setText(
                PesquisaAtual.respostaEspontanea
            )
        }

        btnConfirmarEspontanea.setOnClickListener {
            confirmarResposta()
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

    private fun confirmarResposta() {
        val resposta = etRespostaEspontanea
            .text
            .toString()
            .trim()

        if (resposta.isEmpty()) {
            etRespostaEspontanea.error = getString(
                R.string.espontanea_obrigatoria
            )

            etRespostaEspontanea.requestFocus()
            return
        }

        PesquisaAtual.respostaEspontanea = resposta

        Toast.makeText(
            this,
            R.string.espontanea_guardada,
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}