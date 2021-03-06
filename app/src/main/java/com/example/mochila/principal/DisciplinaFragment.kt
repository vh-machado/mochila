package com.example.mochila.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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

    var listaTarefas: ArrayList<TarefaEntity> = arrayListOf()
    var cardList: MutableList<CardItem> = mutableListOf(CardItem("", "", "", "", 0, false))

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
                if (cardList.isNotEmpty()) {
                    if (cardList[0].titulo == "") {
                        cardList.removeAt(0)
                    }
                }

                var listaDados: MutableList<CardItem> =
                    mutableListOf(CardItem("", "", "", "", 0, false))
                it.forEach {
                    if (it.disciplinaId == idDisciplina) {
                        listaTarefas.add(it)
                        var progresso = 0
                        if (it.checklist.isNotEmpty()) {
                            progresso = (it.checklistConcluido.size * 100) / it.checklist.size
                        }
                        var newCard = CardItem(
                            it.tarefaId,
                            it.disciplinaId,
                            it.titulo,
                            it.dataEntrega,
                            progresso,
                            it.concluido
                        )
                        listaDados.add(newCard)
                        //Log.i("item adicionado", listaDados.toString())
                    }
                }
                // Atualiza RecyclerView
                if (listaDados.size > 1) {
                    listaDados.removeAt(0)
                    cardList = listaDados
                    Log.i("cardList", cardList.toString())
                    cardAdapter = CardAdapter(cardList, this)
                    view.recycler_view.adapter = cardAdapter

                    Log.i("Atualizar RecyclerView", it.toString())

                    cardAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        activity,
                        "Adicione tarefas no bot??o superior direito",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        })

        return view
    }

    override fun onItemClick(position: Int) {
        cardAdapter.notifyItemChanged(position)
        val intent = Intent(activity, TarefaActivity::class.java)
        val adapter = cardAdapter
        intent.putExtra("tarefaDados",listaTarefas[position])
        adapter.notifyDataSetChanged()
        startActivity(intent)

    }

    override fun onCheckBoxClick(position: Int, isChecked: Boolean) {
        val checkedItem: CardItem = cardList[position]
        viewModelTarefa.atualizaConcluido(
            isChecked,
            checkedItem.tarefaId,
            checkedItem.disciplinaId
        )
    }
}