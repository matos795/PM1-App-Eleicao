package com.example.appeleicao

object PesquisaAtual {

    var respostaEspontanea: String = ""
    var respostaEstimulada: Int = 0

    fun limpar() {
        respostaEspontanea = ""
        respostaEstimulada = 0
    }
}