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

    fun lerTarefa(tarefaId: String, disciplinaId: String): LiveData<TarefaEntity> {
        return repository.readTarefa(tarefaId, disciplinaId)
    }

    fun atualizaTitulo(titulo: String, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTituloTarefa(titulo, tarefaId, disciplinaId)
        }
    }

    fun atualizaDescricao(descricao: String, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDescricaoTarefa(descricao, tarefaId, disciplinaId)
        }
    }

    fun atualizaDataEntrega(dataEntrega: String, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDataEntregaTarefa(dataEntrega, tarefaId, disciplinaId)
        }
    }

    fun atualizaConcluido(concluido: Boolean, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateConcluidoTarefa(concluido, tarefaId, disciplinaId)
        }
    }

    fun atualizaEtiquetasEscolhidas(etiquetasEscolhidas: ArrayList<String>, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEtiquetasEscolhidasTarefa(etiquetasEscolhidas, tarefaId, disciplinaId)
        }
    }

    fun atualizaEtiquetasDisponiveis(etiquetasDisponiveis: ArrayList<String>, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEtiquetasDisponiveisTarefa(etiquetasDisponiveis, tarefaId, disciplinaId)
        }
    }

    fun atualizaChecklist(checklist: ArrayList<String>, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChecklistTarefa(checklist, tarefaId, disciplinaId)
        }
    }

    fun atualizaChecklistConcluido(checklistConcluido: ArrayList<String>, tarefaId: String, disciplinaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChecklistConcluidoTarefa(checklistConcluido, tarefaId, disciplinaId)
        }
    }

}

