package com.example.appeleicao

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuEntrevistadorActivity : AppCompatActivity() {

    private lateinit var btnSairConta: Button
    private lateinit var btnPesquisaEspontanea: Button
    private lateinit var btnPesquisaEstimulada: Button
    private lateinit var btnPesquisaProblemas: Button

    private lateinit var btnPesquisaCompleta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_entrevistador)

        btnSairConta = findViewById(R.id.btnSairConta)

        btnPesquisaEspontanea = findViewById(R.id.btnPesquisaEspontanea)

        btnPesquisaEstimulada = findViewById(R.id.btnPesquisaEstimulada)

        btnPesquisaProblemas = findViewById(R.id.btnPesquisaProblemas)

        btnPesquisaCompleta = findViewById(R.id.btnPesquisaCompleta)

        btnPesquisaCompleta.setOnClickListener {
            abrirPesquisaCompleta()
        }

        btnPesquisaProblemas.setOnClickListener {
            abrirPesquisaProblemas()
        }

        btnPesquisaEstimulada.setOnClickListener {
            abrirPesquisaEstimulada()
        }

        btnPesquisaEspontanea.setOnClickListener {
            abrirPesquisaEspontanea()
        }

        btnSairConta.setOnClickListener {
            sairDaConta()
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

    private fun abrirPesquisaEspontanea() {
        PesquisaAtual.iniciar(TipoPesquisa.ESPONTANEA)

        val telaPesquisa = Intent(
            this,
            PesquisaEspontaneaActivity::class.java
        )

        startActivity(telaPesquisa)
    }

    private fun abrirPesquisaEstimulada() {
        PesquisaAtual.iniciar(TipoPesquisa.ESTIMULADA)

        val telaPesquisa = Intent(
            this,
            PesquisaEstimuladaActivity::class.java
        )

        startActivity(telaPesquisa)
    }

    private fun abrirPesquisaProblemas() {
        PesquisaAtual.iniciar(TipoPesquisa.PROBLEMAS)

        val telaPesquisa = Intent(
            this,
            PesquisaProblemasActivity::class.java
        )

        startActivity(telaPesquisa)
    }

    private fun abrirPesquisaCompleta() {
        PesquisaAtual.iniciar(TipoPesquisa.COMPLETA)

        val telaPesquisa = Intent(
            this,
            PesquisaEspontaneaActivity::class.java
        )

        startActivity(telaPesquisa)
    }

    private fun sairDaConta() {
        PesquisaAtual.limpar()

        val telaLogin = Intent(this, MainActivity::class.java)

        telaLogin.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(telaLogin)
    }
}