package com.example.mochila.principal

import android.os.Bundle
import androidx.fragment.app.Fragment

class DisciplinaFragment: Fragment(), CardAdapter.OnItemClickListener{
    var Disciplina: Boolean? = null
    private lateinit var cardAdapter: CardAdapter

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

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }


}