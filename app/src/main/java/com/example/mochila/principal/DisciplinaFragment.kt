package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import com.example.mochila.bancoDados.TarefaEntity
import com.example.mochila.bancoDados.TarefaViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.fragment_disciplina.*
import kotlinx.android.synthetic.main.fragment_disciplina.view.*
import kotlin.random.Random

class DisciplinaFragment : Fragment(), CardAdapter.OnItemClickListener {
    var disciplinaBoolean: Boolean? = null
    var idDisciplina: String? = null
    private lateinit var cardAdapter: CardAdapter

    private lateinit var viewModelTarefa: TarefaViewModel

    var cardList: MutableList<CardItem> = mutableListOf(CardItem("", "", 0))
    val TAG = "ListaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            disciplinaBoolean = arguments?.getBoolean(disciplinaKey)
            idDisciplina = arguments?.getString(idDisciplinaKey)
        }

    }

    companion object {
        private const val disciplinaKey = "disciplina"
        private const val idDisciplinaKey = "idDisciplina"

        fun newInstance(Disciplina: Boolean, IdDisciplina: String): DisciplinaFragment {
            val fragment = DisciplinaFragment()
            val args = Bundle()
            args.putBoolean(disciplinaKey, Disciplina)
            args.putString(idDisciplinaKey, IdDisciplina)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)

        var view = inflater.inflate(R.layout.fragment_disciplina, container, false)
        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        view.recycler_view.setHasFixedSize(true)

        viewModelTarefa.tarefaList.observe(viewLifecycleOwner, Observer<List<TarefaEntity>?> {
            if (it.isNotEmpty()) {
                if(cardList.isNotEmpty()){
                    if(cardList[0].titulo == ""){
                        cardList.removeAt(0)
                    }
                }
                var listaDados: MutableList<CardItem> = mutableListOf(CardItem("", "", 0))
                it.forEach {
                    if(it.disciplinaId == idDisciplina){
                        var newCard = CardItem(
                            it.titulo,
                            it.dataEntrega,
                            0
                        )
                        listaDados.add(newCard)
                        Log.i("item adicionado", listaDados.toString())
                    }
                }
                // Atualiza RecyclerView
                if(listaDados.size > 1){
                    listaDados.removeAt(0)
                    cardList = listaDados
                    Log.i("cardList",cardList.toString())
                    cardAdapter = CardAdapter(cardList,this)
                    view.recycler_view.adapter = cardAdapter

                    Log.i("Atualizar RecyclerView", it.toString())

                    //cardAdapter.setCards(cardList)
                    cardAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(activity, "Adicione tarefas no botão superior direito", Toast.LENGTH_SHORT).show()
                }

            }
        })


        return view
    }
    /*
    fun inserirCard(view: View){
        val index: Int = cardList.size - 1

        val newItem = CardItem(
            "Nova tarefa na posição $index",
            "Data",
            0
        )

        cardList.add(index, newItem)
        cardAdapter.notifyItemInserted(index)
    }
    */

    fun removerItem(view: View) {
        val index: Int = Random.nextInt(8)

        cardList!!.removeAt(index)
        cardAdapter.notifyItemRemoved(index)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Tarefa $position clicada", Toast.LENGTH_SHORT).show()
        val clickedItem: CardItem = cardList!![position]
        clickedItem.titulo = "clicado"
        clickedItem.progresso = clickedItem.progresso + 10
        cardAdapter.notifyItemChanged(position)
        //Passar de uma activity para outra os dados
        /*
        var indice: Int? = null
        viewModelTarefa.tarefaList.value?.forEach{
            if(it.tarefaId == position.toString()){
                indice = position
            }
        }
        val tarefa = viewModelTarefa.tarefaList.value?.get(indice!!)
        Log.i("tarefaList",viewModelTarefa.tarefaList.value?.get(indice!!).toString())
         */
        val intent = Intent(activity,TarefaActivity::class.java)
        val adapter = cardAdapter
        //intent.putExtra("tarefaClicada", tarefa)
        intent.putExtra("idTarefa", position.toString())
        intent.putExtra("idDisciplina", idDisciplina)
        adapter.notifyDataSetChanged()
        startActivity(intent)

    }

    /*
    private fun gerarLista(size: Int): ArrayList<CardItem> {
        val list = ArrayList<CardItem>()
        for (i in 0 until size) {
            val item = CardItem("Tarefa $i", "Data",50)
            list += item
        }
        return list
    }
    */
}