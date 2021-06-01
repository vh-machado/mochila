package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.R
import com.example.mochila.bancoDados.*
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.*

class ListaActivity : AppCompatActivity() {

    private lateinit var viewModelUser: UsersViewModel
    private lateinit var viewModelDisciplinas: DisciplinasViewModel
    private lateinit var viewModelTarefa: TarefaViewModel

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
            viewModelTarefa.saveNewMedia(
                TarefaEntity(
                    "001",
                    "01",
                    "Exercício",
                    "a",
                    "01/06/2021",
                    false,
                    arrayListOf("Cálculo", "Escrita"),
                    arrayListOf("Rasunho", "Caneta"),
                )
            )
            Log.i("Tarefa Adicionada",viewModelTarefa.getTarefas().value.toString())
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


        /*
        val listaCalculo = DisciplinaFragment.newInstance(true)
        val listaProgramacao = DisciplinaFragment.newInstance(true)
        val listaMatematica = DisciplinaFragment.newInstance(true)
        val listaFilosofia = DisciplinaFragment.newInstance(true)
        val listaSociologia = DisciplinaFragment.newInstance(true)
        val listaMetodologia = DisciplinaFragment.newInstance(true)
        adapter.addFragment(listaCalculo, "Cálculo")
        adapter.addFragment(listaProgramacao, "Programação")
        adapter.addFragment(listaMatematica, "Matemática")
        adapter.addFragment(listaFilosofia, "Filosofia")
        adapter.addFragment(listaSociologia, "Sociologia")
        adapter.addFragment(listaMetodologia, "Metodologia")
        */

        viewModelDisciplinas.disciplinasList.observe(this) {
            Log.i("Disciplinas", it.toString())
            if (viewModelDisciplinas.disciplinasList.value != null) {
                val adapter = ViewPagerListaAdapter(supportFragmentManager)

                var dadosDisciplinas = viewModelDisciplinas.disciplinasList.value
                lateinit var disciplina: String
                dadosDisciplinas!!.forEach {
                    disciplina = it.nomeDisciplina
                    var listaDisciplina = DisciplinaFragment.newInstance(true)
                    adapter.addFragment(listaDisciplina, disciplina)
                    Log.i("Disciplina fragmento", it.nomeDisciplina)
                }
                viewPager_Lista.adapter = adapter
                tabLayout_Lista.setupWithViewPager(viewPager_Lista)
                viewPager_Lista.setCurrentItem(0)
                Log.i("Disciplinas", it.toString())
            } else {
                Toast.makeText(
                    this,
                    "Adicione disciplinas no perfil para criar listas.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        /*
        viewModelUser.userList.observe(this){
            Log.i("Usuário", it.toString())
            if (viewModelUser.userList.value?.get(0)?.disciplinas != ""){
                val adapter = ViewPagerListaAdapter(supportFragmentManager)
                var disciplinas = "Lista"
                disciplinas = viewModelUser.userList.value!![0].disciplinas
                var listaDisciplina = DisciplinaFragment.newInstance(true)
                adapter.addFragment(listaDisciplina, disciplinas)
                viewPager_Lista.adapter = adapter
                tabLayout_Lista.setupWithViewPager(viewPager_Lista)
                viewPager_Lista.setCurrentItem(0)
                Log.i("Usuário", it.toString())
                Log.i("Usuário", disciplinas)
            }else{
                Toast.makeText(this,"Adicione disciplinas no perfil para criar listas.", Toast.LENGTH_LONG).show()
            }
        }
        */


    }
}
