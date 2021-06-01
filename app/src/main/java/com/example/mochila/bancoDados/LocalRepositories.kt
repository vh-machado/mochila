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

    /*
    suspend fun updateDisciplinas(disciplinas: String, id: String){
        usersDAO.update(disciplinas, id)
    }
    
    fun getDisciplinasListTask(id: String): List<String> {
        var lerDadosDiciplina: List<String> = usersDAO.getDisciplinasList(id)
        return lerDadosDiciplina
    }
    */
}
class TarefaRepository(private val tarefaDAO: TarefasDAO){

    var readAllData: LiveData<List<TarefaEntity>>

    init{
        readAllData = tarefaDAO.getTarefasList()
    }

    suspend fun saveInTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.saveInTarefasList(tarefa)
    }

    suspend fun removeOfTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.removeOfTarefasList(tarefa)
    }

    suspend fun updateTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.updateTarefas(tarefa)
    }

    suspend fun getAllTarefas(): LiveData<List<TarefaEntity>>{
        return readAllData
    }
}

class DisciplinasRepository(private val disciplinasDAO: DisciplinasDAO){

    val readAllData: LiveData<List<DisciplinasEntity>> = disciplinasDAO.getDisciplinasList()

    suspend fun saveInDisciplinasListTask(disciplina: DisciplinasEntity){
        disciplinasDAO.saveInDisciplinasList(disciplina)
    }

    suspend fun removeOfDisciplinasListTask(disciplina: DisciplinasEntity){
        disciplinasDAO.removeOfDisciplinasList(disciplina)
    }

    suspend fun updateDisciplinasListTask(disciplina: DisciplinasEntity){
        disciplinasDAO.updateDisciplinas(disciplina)
    }

}