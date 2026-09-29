package com.example.appeleicao

object PesquisaAtual {

    var respostaEspontanea: String = ""
    var respostaEstimulada: Int = 0
    var problemasSelecionados: List<Int> = emptyList()

    fun limpar() {
        respostaEspontanea = ""
        respostaEstimulada = 0
        problemasSelecionados = emptyList()
    }
}