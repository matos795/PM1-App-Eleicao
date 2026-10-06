package com.example.appeleicao

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class PainelPesquisaActivity : AppCompatActivity() {
    private lateinit var containerRegistros: LinearLayout
    private lateinit var btnDataInicial: Button
    private lateinit var btnDataFinal: Button
    private var inicio: Long? = null
    private var fim: Long? = null
    private var mostrarResultados = false

    private val candidatos: List<String>
        get() = listOf(
            getString(R.string.candidato_1), getString(R.string.candidato_2),
            getString(R.string.candidato_3), getString(R.string.candidato_4),
            getString(R.string.candidato_5), getString(R.string.voto_branco),
            getString(R.string.voto_nulo), getString(R.string.voto_nao_sei)
        )

    private val problemas: List<String>
        get() = listOf(
            getString(R.string.problema_1), getString(R.string.problema_2),
            getString(R.string.problema_3), getString(R.string.problema_4),
            getString(R.string.problema_5), getString(R.string.problema_6),
            getString(R.string.problema_7), getString(R.string.problema_8),
            getString(R.string.problema_9), getString(R.string.problema_10)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_painel_pesquisa)

        containerRegistros = findViewById(R.id.containerRegistros)
        btnDataInicial = findViewById(R.id.btnDataInicial)
        btnDataFinal = findViewById(R.id.btnDataFinal)
        mostrarResultados = intent.getBooleanExtra("mostrarResultados", false)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("inicio")) {
                inicio = savedInstanceState.getLong("inicio")
            }
            if (savedInstanceState.containsKey("fim")) {
                fim = savedInstanceState.getLong("fim")
            }
        }

        findViewById<TextView>(R.id.tvTituloPainel).setText(
            if (mostrarResultados) {
                R.string.menu_resultados
            } else {
                R.string.menu_entrevistados
            }
        )
        btnDataInicial.setOnClickListener { escolherData(true) }
        btnDataFinal.setOnClickListener { escolherData(false) }
        findViewById<Button>(R.id.btnRemoverFiltro).setOnClickListener {
            inicio = null
            fim = null
            atualizar()
        }
        findViewById<Button>(R.id.btnVoltarPainel).setOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }
        atualizar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        inicio?.let { outState.putLong("inicio", it) }
        fim?.let { outState.putLong("fim", it) }
        super.onSaveInstanceState(outState)
    }

    private fun atualizar() {
        containerRegistros.removeAllViews()
        btnDataInicial.text = getString(
            R.string.painel_data_botao, getString(R.string.painel_inicio),
            inicio?.let { formatarData(it) } ?: getString(R.string.painel_sem_limite)
        )
        btnDataFinal.text = getString(
            R.string.painel_data_botao, getString(R.string.painel_fim),
            fim?.let { formatarData(it) } ?: getString(R.string.painel_sem_limite)
        )

        val limiteInicial = inicio
        val limiteFinal = fim?.let { diaSeguinte(it) }
        val registros = DadosPesquisa.listar().filter {
            (limiteInicial == null || it.dataHora >= limiteInicial) &&
                (limiteFinal == null || it.dataHora < limiteFinal)
        }

        findViewById<TextView>(R.id.tvTotalPainel).text =
            getString(R.string.painel_total, DadosPesquisa.quantidade())
        findViewById<TextView>(R.id.tvTotalPeriodo).text =
            getString(R.string.painel_periodo, registros.size)

        if (registros.isEmpty()) {
            adicionarItem(getString(R.string.painel_vazio))
        } else if (mostrarResultados) {
            exibirResultados(registros)
        } else {
            exibirEntrevistados(registros)
        }
    }

    private fun exibirEntrevistados(registros: List<Entrevista>) {
        for (entrevista in registros.sortedByDescending { it.dataHora }) {
            val tipo = when (entrevista.tipo) {
                TipoPesquisa.ESPONTANEA -> getString(R.string.menu_espontanea)
                TipoPesquisa.ESTIMULADA -> getString(R.string.menu_estimulada)
                TipoPesquisa.PROBLEMAS -> getString(R.string.menu_problemas)
                TipoPesquisa.COMPLETA -> getString(R.string.menu_completa)
            }
            var detalhes = getString(
                R.string.painel_dados, entrevista.celular, tipo,
                formatarData(entrevista.dataHora, true),
                entrevista.latitude.toString(), entrevista.longitude.toString(),
                entrevista.precisaoMetros.toString()
            )
            adicionarItem(entrevista.nome, detalhes)
        }
    }

    private fun exibirResultados(registros: List<Entrevista>) {
        val espontaneas = registros.filter {
            it.tipo == TipoPesquisa.ESPONTANEA || it.tipo == TipoPesquisa.COMPLETA
        }
        adicionarItem(
            getString(R.string.menu_espontanea),
            getString(R.string.painel_base, espontaneas.size)
        )
        if (espontaneas.isEmpty()) {
            adicionarItem(getString(R.string.painel_sem_respostas))
        } else {
            val contagens = espontaneas.groupingBy {
                it.respostaEspontanea.trim().lowercase(Locale.ROOT)
                    .replace(Regex("\\s+"), " ")
            }.eachCount()
            for ((resposta, quantidade) in contagens.entries.sortedByDescending { it.value }) {
                adicionarResultado(resposta, quantidade, espontaneas.size)
            }
        }

        val estimuladas = registros.filter {
            it.tipo == TipoPesquisa.ESTIMULADA || it.tipo == TipoPesquisa.COMPLETA
        }
        adicionarItem(
            getString(R.string.menu_estimulada),
            getString(R.string.painel_base_estimulada, estimuladas.size)
        )
        for (indice in candidatos.indices) {
            val quantidade = estimuladas.count { it.respostaEstimulada == indice + 1 }
            adicionarResultado(candidatos[indice], quantidade, estimuladas.size)
        }

        val pesquisasProblemas = registros.filter {
            it.tipo == TipoPesquisa.PROBLEMAS || it.tipo == TipoPesquisa.COMPLETA
        }
        adicionarItem(
            getString(R.string.menu_problemas),
            getString(R.string.painel_base_problemas, pesquisasProblemas.size)
        )
        for (indice in problemas.indices) {
            val quantidade = pesquisasProblemas.count {
                it.problemasSelecionados.contains(indice + 1)
            }
            adicionarResultado(problemas[indice], quantidade, pesquisasProblemas.size)
        }
    }

    private fun adicionarResultado(nome: String, quantidade: Int, total: Int) {
        val percentual = if (total == 0) 0.0 else quantidade * 100.0 / total
        val percentualTexto = String.format(Locale.getDefault(), "%.1f", percentual)
        adicionarItem(
            nome, getString(R.string.painel_contagem, quantidade, percentualTexto),
            (percentual * 10).roundToInt()
        )
    }

    private fun adicionarItem(titulo: String, detalhes: String = "", progresso: Int? = null) {
        val item = layoutInflater.inflate(
            R.layout.item_painel_pesquisa, containerRegistros, false
        )
        item.findViewById<TextView>(R.id.tvItemTitulo).text = titulo
        val tvDetalhes = item.findViewById<TextView>(R.id.tvItemDetalhes)
        tvDetalhes.text = detalhes
        tvDetalhes.visibility = if (detalhes.isEmpty()) View.GONE else View.VISIBLE
        val barra = item.findViewById<ProgressBar>(R.id.barraResultado)
        if (progresso != null) {
            barra.visibility = View.VISIBLE
            barra.progress = progresso
        }
        containerRegistros.addView(item)
    }

    private fun escolherData(escolhendoInicio: Boolean) {
        val atual = Calendar.getInstance()
        val selecionada = if (escolhendoInicio) inicio else fim
        if (selecionada != null) atual.timeInMillis = selecionada

        DatePickerDialog(this, { _, ano, mes, dia ->
            val calendario = Calendar.getInstance()
            calendario.clear()
            calendario.set(ano, mes, dia, 0, 0, 0)
            val valor = calendario.timeInMillis
            val novoInicio = if (escolhendoInicio) valor else inicio
            val novoFim = if (escolhendoInicio) fim else valor

            if (novoInicio != null && novoFim != null && novoInicio > novoFim) {
                Toast.makeText(this, R.string.painel_datas_invalidas, Toast.LENGTH_LONG).show()
            } else {
                inicio = novoInicio
                fim = novoFim
                atualizar()
            }
        }, atual.get(Calendar.YEAR), atual.get(Calendar.MONTH),
            atual.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun diaSeguinte(data: Long): Long {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = data
        calendario.add(Calendar.DAY_OF_MONTH, 1)
        return calendario.timeInMillis
    }

    private fun formatarData(data: Long, comHora: Boolean = false): String {
        val formato = if (comHora) "dd/MM/yyyy HH:mm:ss" else "dd/MM/yyyy"
        return SimpleDateFormat(formato, Locale.getDefault()).format(Date(data))
    }
}
