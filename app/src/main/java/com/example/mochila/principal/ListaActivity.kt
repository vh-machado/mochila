package com.example.mochila.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import com.example.mochila.bancoDados.UsersEntity
import com.example.mochila.bancoDados.UsersViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.janela_registro_disciplina.*
import java.util.ArrayList
import kotlin.random.Random

class ListaActivity : AppCompatActivity() {

    private lateinit var viewModelUser: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelUser = ViewModelProvider(this).get(UsersViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        setTabs()

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

        botao_menu.setOnClickListener{
            startActivity(Intent(this, RegistroActivity::class.java))
        }
        //Toast.makeText(this, viewModelUser.userList.value, Toast.LENGTH_LONG).show()

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

        //var dadosUsuario: UsersEntity?
        //val position = intent.getSerializableExtra("position") as? Int
        /*
        viewModelUser.userList.observe(this) {
            dadosUsuario = it[0]
            disciplinas = dadosUsuario?.disciplinas.toString()
            Log.i("Fragmento a ser criado", disciplinas)
        }
         */
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
        /*
        var listaDisciplina = DisciplinaFragment.newInstance(true)
        adapter.addFragment(listaDisciplina, disciplinas)
        viewPager_Lista.adapter = adapter
        tabLayout_Lista.setupWithViewPager(viewPager_Lista)
        viewPager_Lista.setCurrentItem(0)
        */

        /*
        val position = intent.getSerializableExtra("position") as? Int
        viewModelUser.userList.observe(this) {
            var userDados = it[position!!]
            var disciplinas = userDados.disciplinas
        }

        var listaDisciplina = DisciplinaFragment.newInstance(true)
        adapter.addFragment(listaDisciplina, disciplinas)

         */
        /*
        var disciplinas: ArrayList<String>? = null
        val usuario = intent.getSerializableExtra("usuario") as? Boolean
        if(usuario == true) {
            val position = intent.getSerializableExtra("position") as? Int
            viewModelUser.userList.observe(this) {
                var userDados = it[position!!]
                disciplinas = userDados.disciplinas
            }
        }
        disciplinas!!.forEach {
            adapter.addFragment(listaDisciplina, it)
        }
        */

        /*
        viewPager_Lista.adapter = adapter
        tabLayout_Lista.setupWithViewPager(viewPager_Lista)
        viewPager_Lista.setCurrentItem(0)
        */

    }
}
