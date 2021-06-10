package com.example.mochila.bancoDados
import androidx.room.ColumnInfo
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
    var etiquetasEscolhidas: ArrayList<String> = arrayListOf(""),
    var etiquetasDisponiveis: ArrayList<String> = arrayListOf(""),
    var checkList: ArrayList<String> = arrayListOf("")
// Permite passar a tarefa toda no putExtra
):Serializable

@Entity
class TituloTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "titulo") private var titulo: String = ""
)

@Entity
class DescricaoTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "descricao") private var descricao: String = ""
)

@Entity
class DataEntregaTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "data_entrega") private var dataEntrega: String = ""
)

@Entity
class ConcluidoTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "concluido") private var concluido: Boolean = false
)

@Entity
class EtiquetasTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "etiquetas") private var etiquetas: ArrayList<String> = arrayListOf("")
)

@Entity
class ChecklistTarefaUpdate(
    @ColumnInfo(name = "id_tarefa") var tarefaId: String = "",
    @ColumnInfo(name = "id_disciplina") var disciplinaId: String = "",
    @ColumnInfo(name = "checklist") private var checklist: ArrayList<String> = arrayListOf("")
)