package com.example.appeleicao

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaEstimuladaActivity : AppCompatActivity() {

    private lateinit var rgCandidatos: RadioGroup
    private lateinit var btnConfirmarEstimulada: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_estimulada)

        rgCandidatos = findViewById(R.id.rgCandidatos)

        btnConfirmarEstimulada = findViewById(
            R.id.btnConfirmarEstimulada
        )

        btnVoltar = findViewById(R.id.btnVoltar)

        if (savedInstanceState == null) {
            restaurarResposta()
        }

        btnConfirmarEstimulada.setOnClickListener {
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
        val idSelecionado = rgCandidatos.checkedRadioButtonId

        val codigoResposta = when (idSelecionado) {
            R.id.rbCandidato1 -> 1
            R.id.rbCandidato2 -> 2
            R.id.rbCandidato3 -> 3
            R.id.rbCandidato4 -> 4
            R.id.rbCandidato5 -> 5
            R.id.rbBranco -> 6
            R.id.rbNulo -> 7
            R.id.rbNaoSei -> 8
            else -> 0
        }

        if (codigoResposta == 0) {
            Toast.makeText(
                this,
                R.string.estimulada_obrigatoria,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        PesquisaAtual.respostaEstimulada = codigoResposta

        Toast.makeText(
            this,
            R.string.estimulada_guardada,
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun restaurarResposta() {
        val idOpcao = when (PesquisaAtual.respostaEstimulada) {
            1 -> R.id.rbCandidato1
            2 -> R.id.rbCandidato2
            3 -> R.id.rbCandidato3
            4 -> R.id.rbCandidato4
            5 -> R.id.rbCandidato5
            6 -> R.id.rbBranco
            7 -> R.id.rbNulo
            8 -> R.id.rbNaoSei
            else -> -1
        }

        if (idOpcao != -1) {
            rgCandidatos.check(idOpcao)
        }
    }
}