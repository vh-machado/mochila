package com.example.mochila.principal

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import com.example.mochila.R
import com.example.mochila.databinding.ActivityTarefaBinding
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.view.*

import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.activity_tarefa.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TarefaActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var dia = 0
    var mes = 0
    var ano = 0
    var diaSalvo = ""
    var mesSalvo = ""
    var anoSalvo = ""
    private lateinit var binding: ActivityTarefaBinding

    // Pertence ao código de etiqueta
    var cout = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        // Código de etiquetas
        binding = ActivityTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chips = intent.getSerializableExtra("chips") as? ArrayList<String>

        var listaChips = arrayListOf<String>()
        chips?.forEach {
            listaChips.add(it)
        }

        setDefaultChips(listaChips)
        Log.i("chips na tarefa:", chips.toString())
        var foraChips = arrayListOf<String>()
        chips?.forEach {
            foraChips.remove(it)
        }
        setDefaultChips(foraChips)

        botAdicionarEtiqueta.setOnClickListener {
            val intent = Intent(this, janelinhaTarefa::class.java)
            startActivity(intent)
        }

        /*
        multilineDescricao.isGone = true
        textDescricao.isGone = false
        botao_salvar_descricao.isGone = true

        textDescricao.setOnClickListener{
            multilineDescricao.isGone = false
            textDescricao.isGone = true
            botao_salvar_descricao.isGone = false
        }

        botao_salvar_descricao.setOnClickListener {
            textDescricao.text = multilineDescricao.text
            multilineDescricao.isGone = true
            textDescricao.isGone = false
            botao_salvar_descricao.isGone = true
        }
         */

        /*
        multilineDescricao.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {


                return@setOnKeyListener true
            }
            false
        }
        */
        layout_input.isGone = true
        botao_add_tarefa_menor.isGone = false

        botao_add_tarefa_menor.setOnClickListener {
            layout_input.isGone = false
            botao_add_tarefa_menor.isGone = true
        }

        inputCheckbox.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val checkbox = CheckBox(this)
                Log.i("inputCheckbox", inputCheckbox.getText().toString())
                checkbox.text = inputCheckbox.text
                //inputCheckbox.imeOptions = EditorInfo.IME_ACTION_NEXT
                inputCheckbox.text.clear()
                linear_layout_tarefas.addView(checkbox as View)

                return@setOnKeyListener true
            }
            false
        }

        botao_fechar_input.setOnClickListener {
            //multilineDescricao.isFocusable = false
            layout_input.isGone = true
            botao_add_tarefa_menor.isGone = false
            //inputCheckbox.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        pickDate()
    }

    private fun getDateCalendar() {
        val cal: Calendar = Calendar.getInstance()
        dia = cal.get(Calendar.DAY_OF_MONTH)
        mes = cal.get(Calendar.MONTH)
        ano = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        textDate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this, ano, mes, dia).show()
        }
    }

    override fun onDateSet(view: DatePicker?, ano: Int, mes: Int, diaDoMes: Int) {
        diaSalvo = String.format("%02d", diaDoMes)
        mesSalvo = String.format("%02d", mes)
        anoSalvo = ano.toString()
        textDate.setText("$diaSalvo/$mesSalvo/$anoSalvo")
    }

    private fun creatChips(name: String, closeIconStatus: Boolean) {
        val chip = Chip(this)
        chip.apply {
            cout += 1
            text = name
            chipIcon = ContextCompat.getDrawable(
                this@TarefaActivity,
                R.drawable.ic_launcher_background
            )
            id = cout
            isChipIconVisible = false
            isCloseIconVisible = closeIconStatus
            isClickable = true
            isCheckable = false
            chip.chipBackgroundColor = getColorStateList(
                R.color.bdazzled_blue
            )
            chip.setTextColor(getResources().getColor(R.color.white))

            binding.apply {
                chipGroupTarefa.addView(chip as View)

                chip.setOnClickListener {
                    creatAlertDialog(chip)


                }

            }
        }
    }

    fun creatAlertDialog(chip: Chip) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(chip.text.toString())
        builder.setMessage("Deseja remover essa etiqueta?")
        builder.setPositiveButton("Sim") { dialog, which ->
            chipGroupTarefa.removeView(chip)
            Toast.makeText(this, "Etiqueta removida", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()

        }
        builder.create().show()

    }

    private fun setDefaultChips(list: List<String>) {
        list.forEach {
            creatChips(it, false)
        }
    }
    //Aqui termina o código de etiqueta


}