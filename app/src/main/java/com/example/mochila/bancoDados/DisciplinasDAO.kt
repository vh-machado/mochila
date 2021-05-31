package com.example.mochila.bancoDados

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DisciplinasDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInDisciplinasList(disciplinas: DisciplinasEntity)

    @Query("SELECT * FROM disciplinastable")
    fun getDisciplinasList(): LiveData<List<DisciplinasEntity>>

    @Delete
    suspend fun removeOfDisciplinasList(disciplinas: DisciplinasEntity)

    @Update
    suspend fun updateDisciplinas(disciplinas: DisciplinasEntity)
}