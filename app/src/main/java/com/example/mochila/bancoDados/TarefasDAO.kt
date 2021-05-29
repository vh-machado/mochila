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

    @Delete
    suspend fun removeOfTarefasList(tarefa: TarefaEntity)
}