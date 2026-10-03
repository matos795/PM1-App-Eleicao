package com.example.appeleicao

object DadosPesquisa {
    private val entrevistas = mutableListOf<Entrevista>()

    fun adicionar(entrevista: Entrevista) {
        entrevistas.add(entrevista)
    }

    fun listar(): List<Entrevista> {
        return entrevistas.toList()
    }

    fun quantidade(): Int {
        return entrevistas.size
    }

    fun limpar() {
        entrevistas.clear()
    }
}