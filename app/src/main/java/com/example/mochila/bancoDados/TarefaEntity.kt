package com.example.mochila.bancoDados
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tarefa")
data class TarefaEntity (
    @PrimaryKey(autoGenerate = false)
    val tarefaId: String = "",
    var disciplinaId: String = "",
    var titulo: String = "",
    var descricao: String = "",
    var dataEntrega: String = "",
    var concluido: Boolean = false,
    var etiquetas: ArrayList<String> = arrayListOf(),
    var checkList: ArrayList<String> = arrayListOf() ,
// Permite passar a tarefa toda no putExtra
):Serializable
