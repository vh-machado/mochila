package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.bancoDados.UsersViewModel
import com.example.mochila.bancoDados.UsersEntity
import com.example.mochila.R
import com.example.mochila.bancoDados.DisciplinasEntity
import com.example.mochila.bancoDados.DisciplinasViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lista.*
import java.util.*
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.botao_menu
import kotlinx.android.synthetic.main.janela_registro_disciplina.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.*

class RegistroActivity : AppCompatActivity() {
    private lateinit var viewModelUser: UsersViewModel
    private lateinit var viewModelDisciplinas: DisciplinasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelUser = ViewModelProvider(this).get(UsersViewModel::class.java)
        viewModelDisciplinas = ViewModelProvider(this).get(DisciplinasViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        textview_username.setText(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        textview_useremail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())

        botao_abre_add_disciplina.setOnClickListener {
            createAlertAddDisciplina()
        }

        botao_menu.setOnClickListener {
            startActivity(Intent(this, ListaActivity::class.java))
        }
    }
    /*
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
*/

    fun createAlertAddDisciplina() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.janela_registro_disciplina, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.show()



        view.botao_add_disciplina.setOnClickListener {
            /*
            dadosDisciplina = listOf<String>(
                campo_nome_disciplina.text.toString(),
                campo_email_professor.text.toString()
            )
             */


            var disciplina = view.campo_nome_disciplina.getText().toString()
            var nomeProfessor = view.campo_nome_professor.getText().toString()
            var emailProfessor = view.campo_email_professor.getText().toString()

            Log.i("Disciplinas não-atualizadas", viewModelDisciplinas.disciplinasList.value.toString())
            // Atualiza a disciplina
            //viewModelUser.atualizaDisciplinas(disciplinas, Firebase.auth.currentUser!!.uid)
            viewModelDisciplinas.saveNewMedia(
                DisciplinasEntity(
                    UUID.randomUUID().toString(),
                    Firebase.auth.currentUser!!.uid,
                    disciplina,
                    nomeProfessor,
                    emailProfessor
                )
            )
            Log.i("Disciplinas atualizadas", viewModelDisciplinas.disciplinasList.value.toString())

            builder.dismiss()
        }

        view.botao_fechar.setOnClickListener {
            builder.dismiss()
        }

    }

}