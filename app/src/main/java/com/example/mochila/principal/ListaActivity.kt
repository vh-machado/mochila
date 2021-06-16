package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.R
import com.example.mochila.bancoDados.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.*
import kotlinx.android.synthetic.main.janela_titulo_tarefa.view.*
import java.util.*

class ListaActivity : AppCompatActivity() {

    private lateinit var viewModelUser: UsersViewModel
    private lateinit var viewModelDisciplinas: DisciplinasViewModel
    private lateinit var viewModelTarefa: TarefaViewModel
    var nomeDisciplina = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelUser = ViewModelProvider(this).get(UsersViewModel::class.java)
        viewModelDisciplinas = ViewModelProvider(this).get(DisciplinasViewModel::class.java)
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        // Definir as abas das listas/disciplinas
        setTabs()

        /*
        botao_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var googleSignInClient: GoogleSignInClient
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
            googleSignInClient.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        */

        botao_menu.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
        //Toast.makeText(this, viewModelUser.userList.value, Toast.LENGTH_LONG).show()

        botao_add_tarefa.setOnClickListener {
            if (viewModelDisciplinas.disciplinasList.value?.isNotEmpty() == true) {
                createAlertCriaTarefa()
            } else {
                Toast.makeText(
                    this,
                    "Adicione disciplinas no perfil antes de criar tarefas.",
                    Toast.LENGTH_LONG
                ).show()
            }


        }

    }

    /*
    fun inserirItem(view: View){
        val index: Int = Random.nextInt(8)

        val newItem = CardItem(
            "Nova tarefa na posição $index",
            "Data",
            0
        )

        cardList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    fun removerItem(view: View){
        val index: Int = Random.nextInt(8)

        cardList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }
    */

    private fun setTabs() {

        viewModelDisciplinas.disciplinasList.observe(this) {
            Log.i("Disciplinas", it.toString())

            if (viewModelDisciplinas.disciplinasList.value?.isNotEmpty() == true) {
                val adapter = ViewPagerListaAdapter(supportFragmentManager)
                var dadosDisciplinas = viewModelDisciplinas.disciplinasList.value
                lateinit var disciplina: String

                dadosDisciplinas!!.forEach {
                    disciplina = it.nomeDisciplina
                    var listaDisciplina = DisciplinaFragment.newInstance(true, it.disciplinaId)
                    adapter.addFragment(listaDisciplina, disciplina)
                    Log.i("Disciplina fragmento", it.nomeDisciplina)
                }

                viewPager_Lista.adapter = adapter
                tabLayout_Lista.setupWithViewPager(viewPager_Lista)

                if (intent.hasExtra("disciplinaNome")) {
                    nomeDisciplina = intent.getStringExtra("disciplinaNome").toString()
                    var cont = adapter.count
                    var tab = ""

                    for (i in 0..(cont - 1)) {
                        tab = viewPager_Lista.adapter?.getPageTitle(i).toString()

                        if (tab == nomeDisciplina) {
                            viewPager_Lista.setCurrentItem(i)
                            break
                        }
                    }
                } else {
                    viewPager_Lista.setCurrentItem(0)
                }

                Log.i("Disciplinas", it.toString())
            } else {
                Toast.makeText(
                    this,
                    "Adicione disciplinas no perfil para criar listas.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun createAlertCriaTarefa() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View = LayoutInflater.from(this).inflate(R.layout.janela_titulo_tarefa, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        //builder.window!!.setLayout(700, 1100)
        builder.show()

        view.botao_cria_tarefa.setOnClickListener {

            var tabNomeDisciplina =
                viewPager_Lista.adapter?.getPageTitle(viewPager_Lista.currentItem).toString()
            var tabIdDisciplina: String? = null
            var tamanhoLista = 0
            var disciplinaDados: DisciplinasEntity? = null
            Log.i("Tab nome", tabNomeDisciplina)
            viewModelDisciplinas.disciplinasList.value?.forEach {
                if (it.nomeDisciplina == tabNomeDisciplina) {
                    tabIdDisciplina = it.disciplinaId
                    tamanhoLista = it.quantidadeTarefa
                    disciplinaDados = it

                }
            }

            viewModelTarefa.saveNewMedia(
                TarefaEntity(
                    tamanhoLista.toString(),
                    tabIdDisciplina!!,
                    view.input_titulo_tarefa.text.toString(),
                    "",
                    "",
                    false,
                    arrayListOf(""),
                    arrayListOf(
                        "Apresentação",
                        "Cálculo",
                        "Escrita",
                        "Estudo",
                        "Leitura",
                        "Pesquisa",
                        "Grupo",
                        "Vídeo",
                        "Vídeo-aula"
                    ),
                    arrayListOf(),
                    arrayListOf()
                )
            )
            Log.i("Tarefa Adicionada", viewModelTarefa.tarefaList.value.toString())
            Log.i("Tarefas salvas", viewModelTarefa.tarefaList.value.toString())
            viewModelDisciplinas.updateMedia(
                DisciplinasEntity(
                    disciplinaDados!!.disciplinaId,
                    disciplinaDados!!.usuarioId,
                    disciplinaDados!!.nomeDisciplina,
                    disciplinaDados!!.nomeProfessor,
                    disciplinaDados!!.emailProfessor,
                    (tamanhoLista + 1)
                )
            )

            val intent = Intent(this, TarefaActivity::class.java)
            intent.putExtra("idTarefa", tamanhoLista.toString())
            intent.putExtra("idDisciplina", disciplinaDados!!.disciplinaId)
            startActivity(intent)
            finish()
            builder.dismiss()
        }

        view.botao_fechar.setOnClickListener {
            builder.dismiss()
        }
    }
}
