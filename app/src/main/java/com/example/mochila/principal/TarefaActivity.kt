package com.example.mochila.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.mochila.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_tarefa.*

class TarefaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)
        botAdicionarEtiqueta.setOnClickListener {
         val intent = Intent(this, janelinhaTarefa::class.java)
            startActivity(intent)
        }

    }




}