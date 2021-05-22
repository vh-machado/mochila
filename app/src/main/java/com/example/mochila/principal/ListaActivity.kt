package com.example.mochila.principal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mochila.R
import kotlinx.android.synthetic.main.activity_lista.*
import kotlin.random.Random

class ListaActivity : AppCompatActivity(), CardAdapter.OnItemClickListener {
    private val cardList = gerarLista(7)
    private val adapter = CardAdapter(cardList,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    fun inserirItem(view: View){
        val index: Int = Random.nextInt(8)

        val newItem = CardItem(
            "Nova tarefa na posição $index",
            "Descrição",
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

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Tarefa $position clicada", Toast.LENGTH_SHORT).show()
        val clickedItem: CardItem = cardList[position]
        clickedItem.text1 = "clicado"
        clickedItem.progresso = clickedItem.progresso + 10
        adapter.notifyItemChanged(position)
    }

    private fun gerarLista(size: Int): ArrayList<CardItem> {
        val list = ArrayList<CardItem>()
        for (i in 0 until size) {
            /*
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_baseline
                1 -> R.drawable.ic_assignment
                else -> R.drawable.ic_sun
            }
             */
            val item = CardItem("Tarefa $i", "Descrição",50)
            list += item
        }
        return list
    }
}