package com.example.mochila.bancoDados
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tarefa")
data class TarefaEntity (
    @PrimaryKey(autoGenerate = false)
    val tarefaId: String,
    var disciplinaId: String,
    var titulo: String,
    var descricao: String,
    var dataEntrega: String,
    var concluido: Boolean,
    var etiquetas: ArrayList<String>,
    var checkList: ArrayList<String>,

)