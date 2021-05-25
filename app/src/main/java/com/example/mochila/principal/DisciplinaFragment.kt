package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.fragment_disciplina.*
import kotlinx.android.synthetic.main.fragment_disciplina.view.*
import kotlin.random.Random

class DisciplinaFragment: Fragment(), CardAdapter.OnItemClickListener{
    var Disciplina: Boolean? = null
    private lateinit var cardAdapter: CardAdapter

    private val cardList = gerarLista(7)
    val TAG = "ListaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null){
            Disciplina = arguments?.getBoolean(disciplina)
        }

    }

    companion object{
        private val disciplina = "disciplina"

        fun newInstance(Disciplina: Boolean): DisciplinaFragment{
            val fragment = DisciplinaFragment()
            val args = Bundle()
            args.putBoolean(disciplina, Disciplina)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_disciplina, container, false)
        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        view.recycler_view.setHasFixedSize(true)
        cardAdapter = CardAdapter(cardList,this)
        view.recycler_view.adapter = cardAdapter
        return view
    }

    fun inserirItem(view: View){
        val index: Int = Random.nextInt(8)

        val newItem = CardItem(
            "Nova tarefa na posição $index",
            "Data",
            0
        )

        cardList.add(index, newItem)
        cardAdapter.notifyItemInserted(index)
    }

    fun removerItem(view: View){
        val index: Int = Random.nextInt(8)

        cardList.removeAt(index)
        cardAdapter.notifyItemRemoved(index)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Tarefa $position clicada", Toast.LENGTH_SHORT).show()
        val clickedItem: CardItem = cardList[position]
        clickedItem.text1 = "clicado"
        clickedItem.progresso = clickedItem.progresso + 10
        cardAdapter.notifyItemChanged(position)
    }

    private fun gerarLista(size: Int): ArrayList<CardItem> {
        val list = ArrayList<CardItem>()
        for (i in 0 until size) {
            val item = CardItem("Tarefa $i", "Data",50)
            list += item
        }
        return list
    }
}