package com.example.mochila.principal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mochila.R
import com.example.mochila.bancoDados.DisciplinasEntity
import kotlinx.android.synthetic.main.card_disciplina_item.view.*

class CardDisciplinaAdapter(val listener: OnDisciplinaClickListener): RecyclerView.Adapter<CardDisciplinaAdapter.CardViewHolder>() {
    private var listaDisciplinas = emptyList<CardDisciplinaItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_disciplina_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = listaDisciplinas[position]
        holder.nomeDisciplina.text = currentItem.nomeDisciplina

    }


    fun setData(listaDisciplinas: List<CardDisciplinaItem>){
        this.listaDisciplinas = listaDisciplinas
        notifyDataSetChanged()
    }

    override fun getItemCount() = listaDisciplinas.size

    interface OnDisciplinaClickListener {
        fun disciplinaClick(position: Int)

    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val nomeDisciplina: TextView = itemView.item_disciplina

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.disciplinaClick(position)
            }
        }
    }
}