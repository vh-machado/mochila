package com.example.mochila.bancoDados
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tarefa")
data class TarefaEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var descricao: String,
    var dataEntrega: Int,
    var concluido: Boolean,
    var etiquetas: ArrayList<String>,
    var checkList: ArrayList<String>,

)