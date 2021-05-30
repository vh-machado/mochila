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

    suspend fun updateUsersListTask(user: UsersEntity){
        usersDAO.updateUsers(user)
    }

    suspend fun updateDisciplinas(disciplinas: String, id: String){
        usersDAO.update(disciplinas, id)
    }

    fun getDisciplinasListTask(id: String): List<String> {
        var lerDadosDiciplina: List<String> = usersDAO.getDisciplinasList(id)
        return lerDadosDiciplina
    }
}