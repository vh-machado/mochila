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
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mochila.R
import com.example.mochila.bancoDados.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.view.*
import kotlinx.android.synthetic.main.janela_titulo_tarefa.view.*
import kotlinx.android.synthetic.main.janela_titulo_tarefa.view.botao_fechar
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

        iv_perfil_miniatura.load(FirebaseAuth.getInstance().currentUser?.photoUrl)
        // Definir as abas das listas/disciplinas
        setTabs()

        iv_perfil_miniatura.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

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

    private fun setTabs() {

        viewModelDisciplinas.disciplinasList.observe(this) {
            //Log.i("Disciplinas", it.toString())

            if (viewModelDisciplinas.disciplinasList.value?.isNotEmpty() == true) {
                val adapter = ViewPagerListaAdapter(supportFragmentManager)
                var dadosDisciplinas = viewModelDisciplinas.disciplinasList.value
                lateinit var disciplina: String

                dadosDisciplinas!!.forEach {
                    disciplina = it.nomeDisciplina
                    var listaDisciplina = DisciplinaFragment.newInstance(true, it.disciplinaId)
                    adapter.addFragment(listaDisciplina, disciplina)
                    //Log.i("Disciplina fragmento", it.nomeDisciplina)
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
        builder.show()

        var textoValido = false
        view.botao_cria_tarefa.setOnClickListener {
            if (textoValido) {
                var tabNomeDisciplina =
                    viewPager_Lista.adapter?.getPageTitle(viewPager_Lista.currentItem).toString()
                var tabIdDisciplina: String? = null
                var tamanhoLista = 0
                var disciplinaDados: DisciplinasEntity? = null
                //Log.i("Tab nome", tabNomeDisciplina)
                viewModelDisciplinas.disciplinasList.value?.forEach {
                    if (it.nomeDisciplina == tabNomeDisciplina) {
                        tabIdDisciplina = it.disciplinaId
                        tamanhoLista = it.quantidadeTarefa
                        disciplinaDados = it

                    }
                }

                var novaTarefaId = UUID.randomUUID().toString()
                viewModelTarefa.saveNewMedia(
                    TarefaEntity(
                        novaTarefaId,
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
                intent.putExtra("idTarefa", novaTarefaId)
                intent.putExtra("idDisciplina", disciplinaDados!!.disciplinaId)
                startActivity(intent)
                finish()
                builder.dismiss()
            } else {
                view.input_layout.error = "Campo obrigatório"
            }
        }

        view.input_titulo_tarefa.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                textoValido = false
                view.input_layout.error = "Campo obrigatório"
            } else {
                textoValido = true
                view.input_layout.error = null
            }
        }

        view.botao_fechar.setOnClickListener {
            builder.dismiss()
        }
    }
}
