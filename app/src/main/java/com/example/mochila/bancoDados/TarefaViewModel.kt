package com.example.mochila.bancoDados

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TarefaViewModel(app: Application) : AndroidViewModel(app) {

    val tarefaList: LiveData<List<TarefaEntity>>
    private val repository: TarefaRepository

    init {
        val tarefaDAO = AppDataBase.getDataBase(app).tarefaDAO()
        repository = TarefaRepository(tarefaDAO)
        tarefaList = repository.readAllData
    }

    fun saveNewMedia(tarefa: TarefaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInTarefasListTask(tarefa)
        }
    }

    fun removeMedia(tarefa: TarefaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOfTarefasListTask(tarefa)
        }
    }

    fun updateMedia(tarefa: TarefaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTarefasListTask(tarefa)
        }
    }

    fun getTarefas(): LiveData<List<TarefaEntity>> {
        return tarefaList
    }
}

