package com.example.mochila.bancoDados

import androidx.room.Dao
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
}