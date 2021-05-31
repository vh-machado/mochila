package com.example.mochila.bancoDados

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel(app: Application): AndroidViewModel(app) {

    val userList: LiveData<List<UsersEntity>>
    private val repository: UsersRepository

    init {
        val usersDAO = AppDataBase.getDataBase(app).usersDAO()
        repository = UsersRepository(usersDAO)
        userList = repository.readAllData
    }

    fun saveNewMedia(user: UsersEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInUsersListTask(user)
        }
    }

    fun removeMedia(user: UsersEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOfUsersListTask(user)
        }
    }

    fun updateMedia(user: UsersEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateUsersListTask(user)
        }
    }

    /*
    fun atualizaDisciplinas(disciplinas: String, id: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateDisciplinas(disciplinas, id)
        }
    }

    fun lerDisciplinas(id: String): List<String> {
        return repository.getDisciplinasListTask(id)
    }
     */
}

