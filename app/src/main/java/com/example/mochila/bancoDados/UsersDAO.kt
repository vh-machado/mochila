package com.example.mochila.bancoDados

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInUsersList(user: UsersEntity)

    @Query("SELECT * FROM userstable")
    fun getUsersList(): LiveData<List<UsersEntity>>

    @Delete
    suspend fun removeOfUsersList(user: UsersEntity)

    @Update
    suspend fun updateUsers(user: UsersEntity)

    // Atualiza a disciplina através do id
    @Query("UPDATE userstable SET disciplinas=:disciplinas WHERE id = :id")
    fun update(disciplinas: String?, id: String)

    // Acessa as disciplinas
    @Query("SELECT disciplinas FROM userstable WHERE id = :id")
    fun getDisciplinasList(id: String): List<String>


}