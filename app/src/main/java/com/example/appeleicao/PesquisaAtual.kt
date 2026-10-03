package com.example.appeleicao

enum class TipoPesquisa {
    ESPONTANEA,
    ESTIMULADA,
    PROBLEMAS,
    COMPLETA
}

object PesquisaAtual {

    var tipo: TipoPesquisa = TipoPesquisa.ESPONTANEA

    var respostaEspontanea: String = ""
    var respostaEstimulada: Int = 0
    var problemasSelecionados: List<Int> = emptyList()

    var nome: String = ""
    var celular: String = ""

    var latitude: Double? = null
    var longitude: Double? = null
    var precisaoMetros: Float? = null
    var dataHora: Long? = null

    fun iniciar(novoTipo: TipoPesquisa) {
        limpar()
        tipo = novoTipo
    }

    fun limpar() {
        tipo = TipoPesquisa.ESPONTANEA
        respostaEspontanea = ""
        respostaEstimulada = 0
        problemasSelecionados = emptyList()
        nome = ""
        celular = ""
        latitude = null
        longitude = null
        precisaoMetros = null
        dataHora = null
    }
}