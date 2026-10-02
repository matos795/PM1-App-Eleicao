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
    }
}