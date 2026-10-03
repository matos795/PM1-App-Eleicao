package com.example.appeleicao

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DadosEntrevistadoActivity : AppCompatActivity() {

    private lateinit var etNomeEntrevistado: EditText
    private lateinit var etCelularEntrevistado: EditText
    private lateinit var btnConfirmarDados: Button
    private lateinit var btnVoltar: Button

    private lateinit var clienteLocalizacao: FusedLocationProviderClient

    private var buscaAtual: CancellationTokenSource? = null

    private val solicitarPermissoes = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissoes ->

        val precisaPermitida =
            permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val aproximadaPermitida =
            permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (precisaPermitida || aproximadaPermitida) {
            confirmarDados()
        } else {
            mostrarMensagem(R.string.localizacao_negada)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados_entrevistado)

        etNomeEntrevistado = findViewById(R.id.etNomeEntrevistado)
        etCelularEntrevistado = findViewById(R.id.etCelularEntrevistado)
        btnConfirmarDados = findViewById(R.id.btnConfirmarDados)
        btnVoltar = findViewById(R.id.btnVoltar)

        clienteLocalizacao = LocationServices.getFusedLocationProviderClient(this)

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
        if (buscaAtual != null) {
            return
        }

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

        if (!temPermissaoLocalizacao()) {
            solicitarPermissoes.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        buscarLocalizacao(nome, celular)
    }

    private fun temPermissaoLocalizacao(): Boolean {
        val precisa = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximada = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return precisa || aproximada
    }

    @SuppressLint("MissingPermission")
    private fun buscarLocalizacao(nome: String, celular: String) {
        if (!temPermissaoLocalizacao()) {
            mostrarMensagem(R.string.localizacao_negada)
            return
        }

        val cancelamento = CancellationTokenSource()
        buscaAtual = cancelamento
        alterarEstadoBusca(true)

        val pedido = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .setDurationMillis(20000)
            .build()

        try {
            clienteLocalizacao.getCurrentLocation(
                pedido,
                cancelamento.token
            ).addOnSuccessListener { localizacao ->

                if (buscaAtual !== cancelamento) {
                    return@addOnSuccessListener
                }

                buscaAtual = null
                alterarEstadoBusca(false)

                if (localizacao == null) {
                    mostrarMensagem(R.string.localizacao_indisponivel)
                } else {
                    val instante = System.currentTimeMillis()

                    PesquisaAtual.nome = nome
                    PesquisaAtual.celular = celular
                    PesquisaAtual.latitude = localizacao.latitude
                    PesquisaAtual.longitude = localizacao.longitude
                    PesquisaAtual.precisaoMetros = localizacao.accuracy
                    PesquisaAtual.dataHora = instante

                    exibirDadosCapturados(instante)
                }

            }.addOnFailureListener {
                if (buscaAtual === cancelamento) {
                    buscaAtual = null
                    alterarEstadoBusca(false)
                    mostrarMensagem(R.string.localizacao_falhou)
                }
            }

        } catch (erro: SecurityException) {
            buscaAtual = null
            cancelamento.cancel()
            alterarEstadoBusca(false)
            mostrarMensagem(R.string.localizacao_negada)
        }
    }

    private fun alterarEstadoBusca(buscando: Boolean) {
        btnConfirmarDados.isEnabled = !buscando
        etNomeEntrevistado.isEnabled = !buscando
        etCelularEntrevistado.isEnabled = !buscando

        btnConfirmarDados.setText(
            if (buscando) {
                R.string.localizacao_buscando
            } else {
                R.string.dados_confirmar
            }
        )
    }

    private fun exibirDadosCapturados(instante: Long) {
        val formato = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        )

        val dataFormatada = formato.format(Date(instante))

        val mensagem = getString(
            R.string.rascunho_detalhes,
            dataFormatada,
            PesquisaAtual.latitude.toString(),
            PesquisaAtual.longitude.toString(),
            PesquisaAtual.precisaoMetros.toString()
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.rascunho_titulo)
            .setMessage(mensagem)
            .setCancelable(false)
            .setPositiveButton(R.string.voltar_menu) { _, _ ->
                voltarAoMenu()
            }
            .show()
    }

    private fun mostrarMensagem(texto: Int) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
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

    override fun onStop() {
        super.onStop()

        val busca = buscaAtual

        if (busca != null) {
            buscaAtual = null
            busca.cancel()
            alterarEstadoBusca(false)
            mostrarMensagem(R.string.localizacao_interrompida)
        }
    }
}