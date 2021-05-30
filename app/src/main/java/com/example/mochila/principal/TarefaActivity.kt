package com.example.mochila.principal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.example.mochila.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_tarefa.*

class TarefaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)
        etiqueta10.setOnClickListener {
                    }

    }
    fun creatAlertAddEtiqueta(){
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val  view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.show()
        builder.dismiss()

    }
}