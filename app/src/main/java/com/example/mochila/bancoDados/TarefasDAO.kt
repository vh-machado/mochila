package com.example.mochila.bancoDados

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TarefasDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInTarefasList(tarefa: TarefaEntity)

    @Query("SELECT * FROM tarefa")
    fun getTarefasList(): LiveData<List<TarefaEntity>>

    @Query("SELECT * FROM tarefa WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun getTarefa(tarefaId: String, disciplinaId: String): LiveData<TarefaEntity>

    @Delete
    suspend fun removeOfTarefasList(tarefa: TarefaEntity)

    @Update
    suspend fun updateTarefas(tarefa: TarefaEntity)

    // Atualização dos dados das funcionalidades da tarefa
    @Query("UPDATE tarefa SET titulo = :titulo WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateTitulo(titulo: String, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET descricao = :descricao WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateDescricao(descricao: String, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET dataEntrega = :dataEntrega WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateDataEntrega(dataEntrega: String, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET concluido = :concluido WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateConcluido(concluido: Boolean, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET etiquetasEscolhidas = :etiquetasEscolhidas WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateEtiquetasEscolhidas(etiquetasEscolhidas: ArrayList<String>, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET etiquetasDisponiveis = :etiquetasDisponiveis WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateEtiquetasDisponiveis(etiquetasDisponiveis: ArrayList<String>, tarefaId: String, disciplinaId: String)

    @Query("UPDATE tarefa SET checklist = :checklist WHERE tarefaId = :tarefaId AND disciplinaId = :disciplinaId")
    fun updateChecklist(checklist: ArrayList<String>, tarefaId: String, disciplinaId: String)
}