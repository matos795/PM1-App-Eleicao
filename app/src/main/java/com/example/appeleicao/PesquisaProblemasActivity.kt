package com.example.appeleicao

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaProblemasActivity : AppCompatActivity() {

    private lateinit var caixasProblemas: List<CheckBox>
    private lateinit var btnConfirmarProblemas: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_problemas)

        caixasProblemas = listOf(
            findViewById<CheckBox>(R.id.cbProblema1),
            findViewById<CheckBox>(R.id.cbProblema2),
            findViewById<CheckBox>(R.id.cbProblema3),
            findViewById<CheckBox>(R.id.cbProblema4),
            findViewById<CheckBox>(R.id.cbProblema5),
            findViewById<CheckBox>(R.id.cbProblema6),
            findViewById<CheckBox>(R.id.cbProblema7),
            findViewById<CheckBox>(R.id.cbProblema8),
            findViewById<CheckBox>(R.id.cbProblema9),
            findViewById<CheckBox>(R.id.cbProblema10)
        )

        btnConfirmarProblemas = findViewById(
            R.id.btnConfirmarProblemas
        )

        btnVoltar = findViewById(R.id.btnVoltar)

        if (savedInstanceState == null) {
            restaurarProblemas()
        }

        btnConfirmarProblemas.setOnClickListener {
            confirmarProblemas()
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

    private fun confirmarProblemas() {
        val selecionados = mutableListOf<Int>()

        for (indice in caixasProblemas.indices) {
            val caixa = caixasProblemas[indice]

            if (caixa.isChecked) {
                selecionados.add(indice + 1)
            }
        }

        if (selecionados.size != 3) {
            val mensagem = getString(
                R.string.problemas_quantidade_invalida,
                selecionados.size
            )

            Toast.makeText(
                this,
                mensagem,
                Toast.LENGTH_LONG
            ).show()

            return
        }

        PesquisaAtual.problemasSelecionados = selecionados.toList()

        Toast.makeText(
            this,
            R.string.problemas_guardados,
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun restaurarProblemas() {
        for (indice in caixasProblemas.indices) {
            val codigoProblema = indice + 1

            caixasProblemas[indice].isChecked =
                PesquisaAtual.problemasSelecionados.contains(
                    codigoProblema
                )
        }
    }
}