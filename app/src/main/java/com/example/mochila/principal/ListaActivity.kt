package com.example.mochila.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lista.*
import kotlin.random.Random

class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
            startActivity(Intent(this, TarefaActivity::class.java))
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

        val adapter = ViewPagerListaAdapter(supportFragmentManager)
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

        viewPager_Lista.adapter = adapter
        tabLayout_Lista.setupWithViewPager(viewPager_Lista)
        viewPager_Lista.setCurrentItem(0)
    }
}
