package com.example.mochila.bancoDados

import androidx.lifecycle.LiveData

class UsersRepository(private val usersDAO: UsersDAO){

    val readAllData: LiveData<List<UsersEntity>> = usersDAO.getUsersList()

    suspend fun saveInUsersListTask(user: UsersEntity){
        usersDAO.saveInUsersList(user)
    }

    suspend fun removeOfUsersListTask(user: UsersEntity){
        usersDAO.removeOfUsersList(user)
    }

}