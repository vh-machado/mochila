package com.example.mochila.bancoDados

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInUsersList(user: UsersEntity)

    @Query("SELECT * FROM userstable")
    fun getUsersList(): LiveData<List<UsersEntity>>

    @Query("SELECT disciplinas FROM userstable")
    fun getDisciplinas(): List<String>

    @Delete
    suspend fun removeOfUsersList(user: UsersEntity)

    @Update
    suspend fun updateUsers(user: UsersEntity)

    @Query("UPDATE userstable SET disciplinas=:disciplinas WHERE id = :id")
    fun update(disciplinas: String?, id: String)


}