package com.example.appeleicao

data class Entrevista (
    val tipo: TipoPesquisa,
    val respostaEspontanea: String,
    val respostaEstimulada: Int,
    val problemasSelecionados: List<Int>,
    val nome: String,
    val celular: String,
    val latitude: Double,
    val longitude: Double,
    val precisaoMetros: Float,
    val dataHora: Long
)