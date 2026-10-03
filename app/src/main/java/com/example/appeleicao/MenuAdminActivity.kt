package com.example.appeleicao

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuAdminActivity : AppCompatActivity() {

    private lateinit var btnSairConta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)

        btnSairConta = findViewById(R.id.btnSairConta)
        findViewById<Button>(R.id.btnEntrevistados).setOnClickListener {
            abrirPainel(false)
        }
        findViewById<Button>(R.id.btnResultados).setOnClickListener {
            abrirPainel(true)
        }
        findViewById<Button>(R.id.btnLimparDados).setOnClickListener {
            confirmarLimpeza()
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

    private fun abrirPainel(resultados: Boolean) {
        val tela = Intent(this, PainelPesquisaActivity::class.java)
        tela.putExtra("mostrarResultados", resultados)
        startActivity(tela)
    }

    private fun confirmarLimpeza() {
        if (DadosPesquisa.quantidade() == 0) {
            Toast.makeText(this, R.string.admin_sem_dados, Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.admin_limpar_titulo)
            .setMessage(getString(R.string.admin_limpar_mensagem, DadosPesquisa.quantidade()))
            .setNegativeButton(R.string.admin_cancelar, null)
            .setPositiveButton(R.string.admin_apagar) { _, _ ->
                DadosPesquisa.limpar()
                PesquisaAtual.limpar()
                Toast.makeText(this, R.string.admin_apagados, Toast.LENGTH_SHORT).show()
            }
            .show()
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