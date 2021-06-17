package com.example.mochila.principal

// Card das tarefas na tela da lista
data class CardItem(
    var tarefaId: String,
    var disciplinaId: String,
    var titulo: String,
    var data: String,
    var progresso: Int,
    var concluido: Boolean){
}