package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.bancoDados.UsersViewModel
import com.example.mochila.bancoDados.UserScope
import com.example.mochila.bancoDados.UsersEntity
import com.example.mochila.R
import java.util.*
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {
    private lateinit var viewModelUser: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelUser = ViewModelProvider(this).get(UsersViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        botaoRegistrar.setOnClickListener{
            var estudante = Usuario(
                nome.getText().toString(),
                email.getText().toString())
            val id = UUID.randomUUID().toString()
            val userScope = UserScope(
                id,
                nome.getText().toString(),
                email.getText().toString()
            )
            /*
            addUserList(userScope)
            viewModelUser.userList.observe(this){
                dados.setText(it.toString())
            }
            */
            val intent = Intent(this, ListaActivity::class.java)
            startActivity(intent)
        }
    }

    fun addUserList(userScope: UserScope) {
        viewModelUser.saveNewMedia(UsersEntity(userScope.id, userScope.nome, userScope.email))
    }

    fun removeUserList(userScope: UserScope) {
        viewModelUser.removeMedia(UsersEntity(userScope.id, userScope.nome, userScope.email))
    }


}