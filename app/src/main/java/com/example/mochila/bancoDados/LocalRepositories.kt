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

    fun readTarefa(tarefaId: String, disciplinaId: String): LiveData<TarefaEntity>{
        return tarefaDAO.getTarefa(tarefaId, disciplinaId)
    }

    suspend fun updateTarefasListTask(tarefa: TarefaEntity){
        tarefaDAO.updateTarefas(tarefa)
    }

    // Atualização dos dados das funcionalidades da tarefa
    fun updateTituloTarefa(titulo: String, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateTitulo(titulo, tarefaId, disciplinaId)
    }

    fun updateDescricaoTarefa(descricao: String, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateDescricao(descricao, tarefaId, disciplinaId)
    }

    fun updateDataEntregaTarefa(dataEntrega: String, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateDataEntrega(dataEntrega, tarefaId, disciplinaId)
    }

    fun updateConcluidoTarefa(concluido: Boolean, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateConcluido(concluido, tarefaId, disciplinaId)
    }

    fun updateEtiquetasEscolhidasTarefa(etiquetasEscolhidas: ArrayList<String>, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateEtiquetasEscolhidas(etiquetasEscolhidas, tarefaId, disciplinaId)
    }

    fun updateEtiquetasDisponiveisTarefa(etiquetasDisponiveis: ArrayList<String>, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateEtiquetasDisponiveis(etiquetasDisponiveis, tarefaId, disciplinaId)
    }

    fun updateChecklistTarefa(checklist: ArrayList<String>, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateChecklist(checklist, tarefaId, disciplinaId)
    }

    fun updateChecklistConcluidoTarefa(checklistConcluido: ArrayList<String>, tarefaId: String, disciplinaId: String){
        tarefaDAO.updateChecklistConcluido(checklistConcluido, tarefaId, disciplinaId)
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