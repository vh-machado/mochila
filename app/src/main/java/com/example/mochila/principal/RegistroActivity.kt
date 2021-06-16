package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import com.example.mochila.bancoDados.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lista.*
import java.util.*
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.botao_menu
import kotlinx.android.synthetic.main.fragment_disciplina.view.*
import kotlinx.android.synthetic.main.janela_dados_disciplina.*
import kotlinx.android.synthetic.main.janela_dados_disciplina.view.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.botao_fechar
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.campo_email_professor
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.campo_nome_professor

class RegistroActivity : AppCompatActivity(), CardDisciplinaAdapter.OnDisciplinaClickListener {
    private lateinit var viewModelUser: UsersViewModel
    private lateinit var viewModelDisciplinas: DisciplinasViewModel
    private lateinit var viewModelTarefa: TarefaViewModel

    val disciplinaAdapter = CardDisciplinaAdapter(this)
    private lateinit var listaDisciplinas: List<CardDisciplinaItem>
    private lateinit var listaTarefas: List<TarefaEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelUser = ViewModelProvider(this).get(UsersViewModel::class.java)
        viewModelDisciplinas = ViewModelProvider(this).get(DisciplinasViewModel::class.java)
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //iv_perfil.setImageResource(FirebaseAuth.getInstance().currentUser?.photoUrl)
        textview_username.setText(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        textview_useremail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())

        botao_abre_add_disciplina.setOnClickListener {
            createAlertAddDisciplina()
        }

        botao_menu.setOnClickListener {
            startActivity(Intent(this, ListaActivity::class.java))
        }

        botao_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var googleSignInClient: GoogleSignInClient
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build()
            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
            googleSignInClient.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModelDisciplinas.disciplinasList.observe(this) {
            var dadosDisciplinas = viewModelDisciplinas.disciplinasList.value
            var cards = ArrayList<CardDisciplinaItem>()

            dadosDisciplinas?.forEach {
                cards.add(
                    CardDisciplinaItem(
                        it.disciplinaId,
                        it.usuarioId,
                        it.nomeDisciplina,
                        it.nomeProfessor,
                        it.emailProfessor
                    )
                )
            }
            Log.i("cardsDisciplinas", cards.toString())
            listaDisciplinas = cards
            Log.i("listaDisciplinas", listaDisciplinas.toString())
            recycler_view_disciplinas.layoutManager = LinearLayoutManager(this)
            recycler_view_disciplinas.setHasFixedSize(true)
            disciplinaAdapter.setData(listaDisciplinas)
            recycler_view_disciplinas.adapter = disciplinaAdapter

        }

        viewModelTarefa.tarefaList.observe(this){
            listaTarefas = it
            Log.i("listaTarefas",listaTarefas.toString())
        }

    }

    fun createAlertAddDisciplina() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.janela_registro_disciplina, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.window!!.setLayout(700, 1240)
        builder.show()



        view.botao_add_disciplina.setOnClickListener {

            var disciplina = view.campo_nome_disciplina.getText().toString()
            var nomeProfessor = view.campo_nome_professor.getText().toString()
            var emailProfessor = view.campo_email_professor.getText().toString()

            Log.i(
                "Disciplinas não-atualizadas",
                viewModelDisciplinas.disciplinasList.value.toString()
            )
            // Atualiza a disciplina
            //viewModelUser.atualizaDisciplinas(disciplinas, Firebase.auth.currentUser!!.uid)
            viewModelDisciplinas.saveNewMedia(
                DisciplinasEntity(
                    UUID.randomUUID().toString(),
                    Firebase.auth.currentUser!!.uid,
                    disciplina,
                    nomeProfessor,
                    emailProfessor,
                    0

                )
            )
            Log.i("Disciplinas atualizadas", viewModelDisciplinas.disciplinasList.value.toString())

            builder.dismiss()
        }

        view.botao_fechar.setOnClickListener {
            builder.dismiss()
        }

    }

    fun createAlertDadosDisciplina(dadosDisciplina: CardDisciplinaItem) {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.janela_dados_disciplina, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        //builder.window!!.setLayout(700, 1180)
        builder.show()

        view.text_nome_disciplina.setText(dadosDisciplina.nomeDisciplina)
        view.text_nome_disciplina.inputType = InputType.TYPE_NULL
        view.text_nome_professor.setText(dadosDisciplina.nomeProfessor)
        view.text_nome_professor.inputType = InputType.TYPE_NULL
        view.text_email_professor.setText(dadosDisciplina.emailProfessor)
        view.text_email_professor.inputType = InputType.TYPE_NULL

        view.botao_fechar.setOnClickListener {
            builder.dismiss()
        }

        view.botao_excluir_disciplina.setOnClickListener {
            view.botao_confirmar_excluir_disciplina.visibility = VISIBLE
            view.botao_excluir_disciplina.visibility = INVISIBLE
        }

        view.botao_confirmar_excluir_disciplina.setOnClickListener {
            listaTarefas.forEach {
                if (it.disciplinaId == dadosDisciplina.idDisciplina) {
                    viewModelTarefa.removeTarefa(it)
                }
            }
            Log.i("Tarefas depois da disciplina excluída", listaTarefas.toString())
            viewModelDisciplinas.removeMedia(
                DisciplinasEntity(
                    dadosDisciplina.idDisciplina,
                    dadosDisciplina.idUsuario,
                    dadosDisciplina.nomeDisciplina,
                    dadosDisciplina.nomeProfessor,
                    dadosDisciplina.emailProfessor
                )
            )
            Log.i(
                "Disciplinas pós disciplina excluída",
                viewModelDisciplinas.disciplinasList.value.toString()
            )


            builder.dismiss()
        }
    }

    override fun disciplinaClick(position: Int) {
        val adapter = disciplinaAdapter
        disciplinaAdapter.notifyItemChanged(position)
        Log.i("Position", position.toString())
        Log.i("Disciplina selecionada", listaDisciplinas[position].toString())
        val clickedDisciplina: CardDisciplinaItem = listaDisciplinas[position]
        adapter.notifyDataSetChanged()
        createAlertDadosDisciplina(clickedDisciplina)
    }

    /*
    fun AlertAvatar(){

        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.janela_registro_disciplina, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.show()
    }
     */

}