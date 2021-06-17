package com.example.mochila.principal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mochila.R
import com.example.mochila.bancoDados.TarefaEntity
import com.example.mochila.bancoDados.TarefaViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.card_item.view.*

class CardAdapter(
    private var cardList: MutableList<CardItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)

        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardList[position]

        holder.tarefaTitulo.text = currentItem.titulo
        holder.tarefaData.text = currentItem.data
        holder.progressTask.setProgress(currentItem.progresso, true)
        holder.checkTask.isChecked = currentItem.concluido
    }

    override fun getItemCount() = cardList.size

    fun setCards(tarefas: MutableList<CardItem>) {
        this.cardList = tarefas
        notifyDataSetChanged()
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tarefaTitulo: TextView = itemView.tarefa_titulo
        val tarefaData: TextView = itemView.tarefa_data
        val progressTask: ProgressBar = itemView.progress_task
        val checkTask: CheckBox = itemView.tarefa_check

        init {
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
            checkTask.setOnClickListener{
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCheckBoxClick(position, checkTask.isChecked)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onCheckBoxClick(position: Int, isChecked: Boolean)
    }

}