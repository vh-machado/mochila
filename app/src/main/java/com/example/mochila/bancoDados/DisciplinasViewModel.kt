package com.example.mochila.bancoDados

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisciplinasViewModel(app: Application): AndroidViewModel(app) {

    val disciplinasList: LiveData<List<DisciplinasEntity>>
    private val repository: DisciplinasRepository

    init {
        val disciplinasDAO = AppDataBase.getDataBase(app).disciplinasDAO()
        repository = DisciplinasRepository(disciplinasDAO)
        disciplinasList = repository.readAllData
    }

    fun saveNewMedia(disciplina: DisciplinasEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInDisciplinasListTask(disciplina)
        }
    }

    fun removeMedia(disciplina: DisciplinasEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOfDisciplinasListTask(disciplina)
        }
    }

    fun updateMedia(disciplina: DisciplinasEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateDisciplinasListTask(disciplina)
        }
    }
}
