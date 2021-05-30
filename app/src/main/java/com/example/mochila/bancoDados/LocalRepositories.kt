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
class TarefaRepository(private val tarefaDAO: TarefasDAO){

    val readAllData: LiveData<List<TarefaEntity>> = tarefaDAO.getTarefasList()

    suspend fun saveInTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.saveInTarefasList(tarefa)
    }

    suspend fun removeOfTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.removeOfTarefasList(tarefa)
    }

    suspend fun updateTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.updateTarefas(tarefa)
    }

}