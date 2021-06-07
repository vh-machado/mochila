package com.example.mochila.principal

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.R
import com.example.mochila.bancoDados.DisciplinasEntity
import com.example.mochila.bancoDados.DisciplinasViewModel
import com.example.mochila.bancoDados.TarefaEntity
import com.example.mochila.bancoDados.TarefaViewModel
import com.example.mochila.databinding.ActivityTarefaBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.view.*

import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.activity_tarefa.view.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TarefaActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModelTarefa: TarefaViewModel
    private lateinit var viewModelDisciplinas: DisciplinasViewModel

    var tarefaAtual: TarefaEntity? = null

    var dia = 0
    var mes = 0
    var ano = 0
    var diaSalvo = ""
    var mesSalvo = ""
    var anoSalvo = ""
    private lateinit var binding: ActivityTarefaBinding

    // Pertence ao código de etiqueta
    var cout = 0
    var cout1 = 0
    var chips: ArrayList<String>? = null
    var checklist: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)
        viewModelDisciplinas = ViewModelProvider(this).get(DisciplinasViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        // Código de etiquetas
        binding = ActivityTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //var chips = intent.getSerializableExtra("chips") as? ArrayList<String>
        /*
        val tarefaSelecionada = intent.getSerializableExtra("tarefaClicada") as? TarefaEntity
        val tarefaCriada = intent.getSerializableExtra("tarefaNova") as? TarefaEntity

        if (tarefaSelecionada != null) {
            tarefaAtual = tarefaSelecionada
        } else if (tarefaCriada != null) {
            tarefaAtual = tarefaCriada
        }
        */

        var idTarefa = intent.getSerializableExtra("idTarefa") as String
        var idDisciplina = intent.getSerializableExtra("idDisciplina") as String
        Log.i("idTarefa", idTarefa)
        Log.i("idDisciplina", idDisciplina)
        tarefaAtual = viewModelTarefa.lerTarefa(idTarefa, idDisciplina).value
        /*
        viewModelTarefa.tarefaList.observe(this,{})
        Log.i("tarefaList", viewModelTarefa.tarefaList.value.toString())
        viewModelTarefa.tarefaList.value?.forEach {
            Log.i("Tarefa listada", it.toString())
            if (it.tarefaId == idTarefa && it.disciplinaId == idDisciplina) {
                tarefaAtual = it
            }
        }
        */

        // Setando os Dados Armazenados da Tarefa
        textView_lista_disciplina.setText(idDisciplina)
        titulo_tarefa.setText(tarefaAtual!!.titulo)
        multilineDescricao.setText(tarefaAtual!!.descricao)
        textDate.setText(tarefaAtual!!.dataEntrega)
        chips = tarefaAtual!!.etiquetas

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
            createAlertAddEtiqueta()
        }
        botao_menu.setOnClickListener {
            val intent = Intent(this, ListaActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Manipulação do Título da Tarefa
        titulo_tarefa.inputType = InputType.TYPE_NULL
        titulo_tarefa.setOnTouchListener { v, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {

                titulo_tarefa.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                titulo_tarefa.isCursorVisible = true
                titulo_tarefa.isFocusableInTouchMode = true
                Log.i("Titulo inputype != 0", titulo_tarefa.inputType.toString())

                titulo_tarefa.setOnKeyListener { v1, keyCode, event1 ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event1.action == KeyEvent.ACTION_UP) {
                        //Atualização do Título da Tarefa no Banco de Dados
                        viewModelTarefa.atualizaTitulo(
                            titulo_tarefa.text.toString(),
                            tarefaAtual!!.tarefaId,
                            tarefaAtual!!.disciplinaId
                        )
                        titulo_tarefa.inputType = InputType.TYPE_NULL
                        Log.i("Titulo inputype == 0", titulo_tarefa.inputType.toString())

                        return@setOnKeyListener true
                    }
                    false
                }

                return@setOnTouchListener true
            } else if (event.action == KeyEvent.ACTION_UP) {
                v.performClick()
                return@setOnTouchListener false
            }
            false
        }

        // Manipulação do MultiLine EditText da Descrição da tarefa
        botao_salvar_descricao.visibility = INVISIBLE
        botao_editar_descricao.visibility = VISIBLE
        //multilineDescricao.setRawInputType(EditorInfo.TYPE_NULL)
        multilineDescricao.isEnabled = false

        botao_editar_descricao.setOnClickListener {
            botao_editar_descricao.visibility = INVISIBLE
            botao_salvar_descricao.visibility = VISIBLE
            multilineDescricao.isCursorVisible = true
            multilineDescricao.isFocusableInTouchMode = true
            //multilineDescricao.setRawInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE)
            multilineDescricao.isEnabled = true
            multilineDescricao.requestFocus()
        }

        botao_salvar_descricao.setOnClickListener {
            botao_salvar_descricao.visibility = INVISIBLE
            botao_editar_descricao.visibility = VISIBLE
            //multilineDescricao.setRawInputType(EditorInfo.TYPE_NULL)
            //Atualização da Descrição da Tarefa no Banco de Dados
            viewModelTarefa.atualizaDescricao(
                multilineDescricao.text.toString(),
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )
            multilineDescricao.isEnabled = false
        }

        // Manipulação do Checklist da Tarefa
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
                checklist?.add(inputCheckbox.text.toString())
                Log.i("Checklist", checklist.toString())
                //Atualização do Checklist da Tarefa no Banco de Dados
                viewModelTarefa.atualizaChecklist(
                    checklist!!,
                    tarefaAtual!!.tarefaId,
                    tarefaAtual!!.disciplinaId
                )

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

        // Código para o e-mail
        botao_contato.setOnClickListener {
            /*
              val destinatario = recebe.text.toString().trim()
             */
            //val quemEnvia = "vivibraz045@gmail.com"


            val destinatario = ("vh.machado.silva@gmail.com,marcushuriel80@gmail.com").trim()
            // method call for email intent with these inputs as parameters

            getContato(destinatario)
        }
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
        var data = "$diaSalvo/$mesSalvo/$anoSalvo"
        textDate.setText(data)

        //Atualização da Data de Entrega da Tarefa no Banco de Dados
        viewModelTarefa.atualizaDataEntrega(
            data,
            tarefaAtual!!.tarefaId,
            tarefaAtual!!.disciplinaId
        )
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

    fun createAlertAddEtiqueta() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.window!!.setLayout(700, 1100)
        builder.show()

        var chipsDisponiveis: ArrayList<String> = arrayListOf("")

        fun creatChips1(name: String, closeIconStatus: Boolean) {
            //val view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
            chipsDisponiveis.removeIf {
                it == ""
            }
            chipsDisponiveis.add(name)
            Log.i("Chips disponíveis", chipsDisponiveis.toString())
            val chip = Chip(this)
            chip.apply {
                cout1 = cout1 + 1
                text = name
                chipIcon = ContextCompat.getDrawable(
                    this@TarefaActivity,
                    R.drawable.ic_launcher_background
                )
                id = cout1
                isChipIconVisible = false
                isCloseIconVisible = closeIconStatus
                isClickable = true
                isCheckable = true

                chip.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )

                chip.setTextColor(getResources().getColor(R.color.white))

                view.chipGroup.addView(chip as View)

                chip.setOnCloseIconClickListener {
                    view.chipGroup.removeView(chip as View)
                }

                Log.i("Chip criado", chip.text.toString())

            }
        }

        fun getOnListIdChips(): ArrayList<String> {
            //val view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
            var etiCheck = ArrayList<String>()
            view.chipGroup.checkedChipIds.forEach {
                var chip = view.chipGroup.findViewById<Chip>(it)
                etiCheck.add(chip.text.toString())

            }
            return etiCheck

        }

        fun etiquetaRepetida(etiqueta: String): Boolean {
            chipsDisponiveis.forEach {
                if (it == etiqueta) {
                    Toast.makeText(this, "Essa etiqueta já existe", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            return false
        }

        fun campoVazio(): Boolean {
            val eti = view.infoEtiqueta.text.toString()
            if (eti.isNullOrBlank() == true) {
                Toast.makeText(
                    this,
                    "Para criar etiquetas é necessário informar um nome! Verifique se o campo foi preenchido",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            } else {
                return false
            }
        }

        fun entryChip() {
            view.infoEtiqueta.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    val name = view.infoEtiqueta.text.toString()
                    if (!campoVazio() && !etiquetaRepetida(name)) {
                        creatChips1(name, true)
                        Log.i("Novo chip", view.infoEtiqueta.text.toString())
                        view.infoEtiqueta.text!!.clear()
                    }

                    return@setOnKeyListener true
                }
                false
            }

        }

        fun setDefaultChips1(list: List<String>) {
            list.forEach {
                creatChips1(it, false)

            }
        }

        fun setListChips(): List<String> {
            return listOf(
                "Apresentação",
                "Cálculo",
                "Escrita",
                "Estudo",
                "Leitura",
                "Pesquisa",
                "Grupo",
                "Vídeo",
                "Vídeo-aula"
            )

        }

        setDefaultChips1(setListChips())
        entryChip()

        view.botSalvar.setOnClickListener {
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            chips = getOnListIdChips()
            Log.i("Chips selecionados", chips.toString())
            //shareInfosChipsIntent(getOnListIdChips())
            var listaChips = arrayListOf<String>()
            chips?.forEach {
                listaChips.add(it)
            }
            setDefaultChips(listaChips)

            //Atualização das Etiquetas da Tarefa no Banco de Dados
            viewModelTarefa.atualizaEtiquetas(
                listaChips,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )

            builder.dismiss()

        }
        view.botFechar.setOnClickListener {
            builder.dismiss()
        }
    }

    /*
    fun creatChips1(name: String, closeIconStatus: Boolean) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
        val chip = Chip(this)
        chip.apply {
            cout1 = cout1 + 1
            text = name
            chipIcon = ContextCompat.getDrawable(
                this@TarefaActivity,
                R.drawable.ic_launcher_background
            )
            id = cout1
            isChipIconVisible = false
            isCloseIconVisible = closeIconStatus
            isClickable = true
            isCheckable = true

            chip.chipBackgroundColor = getColorStateList(
                R.color.bdazzled_blue
            )

            chip.setTextColor(getResources().getColor(R.color.white))

            view.chipGroup.addView(chip as View)

            chip.setOnCloseIconClickListener {
                view.chipGroup.removeView(chip as View)
            }

            Log.i("Chip criado", chip.text.toString())

        }
    }
    */


    /*
    private fun shareInfosChipsIntent(chips: ArrayList<String>){
        val intent = Intent(this, TarefaActivity:: class.java)
        intent.putExtra("chips",chips)
        startActivity(intent)
        finish()
    }
    */
    //ARMAZENAR

    private fun setDefaultChips(list: List<String>) {
        list.forEach {
            creatChips(it, false)
        }
    }
    //Aqui termina o código de etiqueta

    //Código para enviar e-mail para o professor
    private fun getContato(destinatario: String) {
        /*ACTION_SEND action to launch an email cliente installed on your Android device */
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData()nethid and
        data type will be to text / plain using setTyp() method */
        mIntent.data = Uri.parse("mailto")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /*recipient is put as array because you nay wanna send email to multiple emails so enter comma(,) separated,
        it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
        //put the subject in the intent

        //mIntent.putExtra(Intent.EXTRA_SUBJECT, sujeito)
        try {
            // start email intent
            startActivity(Intent.createChooser(mIntent, "Choose email cliente..."))

        } catch (e: Exception) {
            // if any thing goes wrong for example no email client application or any exception
            // get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

        }
    }

}