package com.example.mochila.principal

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mochila.R
import com.example.mochila.bancoDados.DisciplinasViewModel
import com.example.mochila.bancoDados.TarefaEntity
import com.example.mochila.bancoDados.TarefaViewModel
import com.example.mochila.bancoDados.TituloTarefaUpdate
import com.example.mochila.databinding.ActivityTarefaBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.view.*

import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.activity_tarefa.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
    var tarefaConcluida = false
    var chips: ArrayList<String>? = null
    var chipsDisponiveis: ArrayList<String>? = arrayListOf("")
    var checklist: ArrayList<String>? = null
    var checklistConcluido: ArrayList<String>? = null
    var checkboxText = ""
    var editChecklist = false
    var disciplinaNome = ""
    var emailProfessor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)
        viewModelDisciplinas = ViewModelProvider(this).get(DisciplinasViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        binding = ActivityTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun defineDisciplina(disciplinaId: String) {
            viewModelDisciplinas.disciplinasList.observe(this, {
                it.forEach {
                    if (disciplinaId == it.disciplinaId) {
                        Log.i("Tarefa da disciplina", it.nomeDisciplina)
                        disciplinaNome = it.nomeDisciplina.uppercase()
                        textView_lista_disciplina.setText("•   $disciplinaNome")
                        emailProfessor = it.emailProfessor
                    }
                }
            })
        }

        var idTarefa = intent.getSerializableExtra("idTarefa") as String
        var idDisciplina = intent.getSerializableExtra("idDisciplina") as String
        Log.i("idTarefa", idTarefa)
        Log.i("idDisciplina", idDisciplina)
        //viewModelTarefa.tarefaList
        defineDisciplina(idDisciplina)
        viewModelTarefa.tarefaList.observe(this, {
            it.forEach {
                Log.i("Tarefa listada", it.toString())
                if (it.tarefaId == idTarefa && it.disciplinaId == idDisciplina) {
                    setDados(it)
                }
            }
            Log.i("tarefaList", viewModelTarefa.tarefaList.value.toString())

        })

        botAdicionarEtiqueta.setOnClickListener {
            createAlertAddEtiqueta()
        }
        botao_menu.setOnClickListener {
            val intent = Intent(this, ListaActivity::class.java)
            intent.putExtra("disciplinaNome", disciplinaNome)
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
        layout_input_checklist.isGone = true
        botao_add_checkbox.isGone = false
        botao_editar_checklist.visibility = VISIBLE
        botao_salvar_checklist.visibility = INVISIBLE

        botao_editar_checklist.setOnClickListener {
            botao_salvar_checklist.visibility = VISIBLE
            botao_editar_checklist.visibility = INVISIBLE
            editChecklist = true
            //Atualização do Checklist da Tarefa no Banco de Dados
            // Provoca a atualização dos botões
            viewModelTarefa.atualizaChecklist(
                checklist!!,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )
        }

        botao_salvar_checklist.setOnClickListener {
            botao_salvar_checklist.visibility = INVISIBLE
            botao_editar_checklist.visibility = VISIBLE
            editChecklist = false
            //Atualização do Checklist da Tarefa no Banco de Dados
            viewModelTarefa.atualizaChecklist(
                checklist!!,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )
        }

        botao_add_checkbox.setOnClickListener {
            layout_input_checklist.isGone = false
            botao_add_checkbox.isGone = true

            entradaChecklist()
        }

        botao_fechar_input_checklist.setOnClickListener {
            //multilineDescricao.isFocusable = false
            layout_input_checklist.isGone = true
            botao_add_checkbox.isGone = false
            inputCheckbox.text.clear()
            //inputCheckbox.imeOptions = EditorInfo.IME_ACTION_DONE
        }



        pickDate()

        // Código para o e-mail
        botao_contato.setOnClickListener {
            /*
              val destinatario = recebe.text.toString().trim()
             */
            //val quemEnvia = (Firebase.auth.currentUser!!.email)?.trim()
            val destinatario = (emailProfessor).trim()
            val assunto = tarefaAtual?.titulo
            // method call for email intent with these inputs as parameters

            getContato(destinatario, assunto!!)
        }

        cardConcluir.setOnClickListener {
            AlertConcluir()

        }


    }

    private fun setDados(tarefa: TarefaEntity) {
        tarefaAtual = tarefa
        titulo_tarefa.setText(tarefaAtual?.titulo)
        multilineDescricao.setText(tarefaAtual?.descricao)
        textDate.setText(tarefaAtual?.dataEntrega)
        tarefaConcluida = tarefaAtual?.concluido == true
        chips = tarefaAtual?.etiquetasEscolhidas
        chipsDisponiveis = tarefaAtual?.etiquetasDisponiveis
        checklist = tarefaAtual?.checklist
        checklistConcluido = tarefaAtual?.checklistConcluido

        if (tarefaConcluida){
            textview_concluir.setText("Concluído")
            cardConcluir.setCardBackgroundColor(getColorStateList(R.color.Cinza))
        }

        chipGroupTarefa.removeAllViews()
        chips?.removeIf { i ->
            i == ""
        }
        var listaChips = arrayListOf<String>()
        chips?.forEach {
            listaChips.add(it)
        }
        setEtiquetas(listaChips)
        Log.i("chips na tarefa:", chips.toString())
        /*
        var foraChips = arrayListOf<String>()
        chips?.forEach {
            foraChips.remove(it)
        }
        setEtiquetas(foraChips)
        */

        linear_layout_checklist.removeAllViews()
        checklist?.removeIf { i ->
            i == ""
        }
        var listaCheckbox = arrayListOf<String>()
        checklist?.forEach {
            listaCheckbox.add(it)
        }
        setCheckList(listaCheckbox)
        Log.i("Checklist na tarefa:", checklist.toString())
    }

    // Funções do PickDate
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

    private fun criaEtiquetas(name: String, closeIconStatus: Boolean) {
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
                    createAlertRemoverEtiqueta(chip)
                }

            }
        }
    }

    fun createAlertRemoverEtiqueta(chip: Chip) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(chip.text.toString())
        builder.setMessage("Deseja remover essa etiqueta?")
        builder.setPositiveButton("Sim") { dialog, which ->
            chipGroupTarefa.removeView(chip)
            chips?.remove(chip.text.toString())
            var chipsEscolhidos = arrayListOf<String>()
            chips?.forEach {
                chipsEscolhidos.add(it)
            }
            //Atualização das Etiquetas Escolhidas da Tarefa no Banco de Dados
            viewModelTarefa.atualizaEtiquetasEscolhidas(
                chipsEscolhidos,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )
            Toast.makeText(this, "Etiqueta removida", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()

        }
        builder.create().show()

    }

    fun AlertConcluir() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo_tarefa.text.toString())
        builder.setMessage("Deseja concluir essa tarefa?")
        builder.setPositiveButton("Sim") { dialog, which ->

            Toast.makeText(this, "Tarefa concluida", Toast.LENGTH_SHORT).show()
            viewModelTarefa.atualizaConcluido(
                true,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )

        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()

        }
        builder.create().show()

    }

    var etiquetasPadrao = arrayListOf<String>(
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

    fun createAlertAddEtiqueta() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View = LayoutInflater.from(this).inflate(R.layout.activity_janelinha_tarefa, null)
        builder.setView(view)
        var window = builder.window
        window!!.setGravity(Gravity.CENTER)
        builder.window!!.attributes.windowAnimations = R.style.DialogAnimation
        builder.window!!.setLayout(700, 1100)
        builder.show()

        fun criaEtiquetaEscolha(name: String, closeIconStatus: Boolean) {

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

                if (chips?.contains(name) == true) {
                    isChecked = true
                }

                chip.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )

                chip.setTextColor(getResources().getColor(R.color.white))

                view.chipGroup.addView(chip as View)

                chip.setOnCloseIconClickListener {
                    view.chipGroup.removeView(chip as View)
                    chipsDisponiveis?.remove(chip.text.toString())
                    //Atualização das Etiquetas Disponíveis da Tarefa no Banco de Dados
                    viewModelTarefa.atualizaEtiquetasDisponiveis(
                        chipsDisponiveis!!,
                        tarefaAtual!!.tarefaId,
                        tarefaAtual!!.disciplinaId
                    )
                }

                Log.i("Chip criado", chip.text.toString())

            }
        }

        fun getEtiquetasEscolhidas(): ArrayList<String> {
            var etiCheck = ArrayList<String>()
            view.chipGroup.checkedChipIds.forEach {
                var chip = view.chipGroup.findViewById<Chip>(it)
                etiCheck.add(chip.text.toString())

            }
            return etiCheck

        }

        fun etiquetaRepetida(etiqueta: String): Boolean {
            chipsDisponiveis?.forEach {
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

        fun entradaNovaEtiqueta() {
            view.infoEtiqueta.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    val name = view.infoEtiqueta.text.toString()
                    if (!campoVazio() && !etiquetaRepetida(name)) {

                        chipsDisponiveis?.removeIf { i ->
                            i == ""
                        }
                        chipsDisponiveis?.add(name)

                        //Atualização das Etiquetas Disponíveis da Tarefa no Banco de Dados
                        viewModelTarefa.atualizaEtiquetasDisponiveis(
                            chipsDisponiveis!!,
                            tarefaAtual!!.tarefaId,
                            tarefaAtual!!.disciplinaId
                        )

                        criaEtiquetaEscolha(name, true)
                        Log.i("Novo chip", view.infoEtiqueta.text.toString())
                        view.infoEtiqueta.text!!.clear()
                    }

                    return@setOnKeyListener true
                }
                false
            }

        }

        fun setEtiquetasEscolha(list: ArrayList<String>) {
            list.forEach {
                if (etiquetasPadrao.contains(it)) {
                    criaEtiquetaEscolha(it, false)
                } else {
                    criaEtiquetaEscolha(it, true)
                }
            }
        }

        setEtiquetasEscolha(chipsDisponiveis!!)
        entradaNovaEtiqueta()

        view.botSalvarEtiquetas.setOnClickListener {
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            chips = getEtiquetasEscolhidas()
            Log.i("Chips selecionados", chips.toString())
            //shareInfosChipsIntent(getOnListIdChips())
            var chipsEscolhidos = arrayListOf<String>()
            chips?.forEach {
                chipsEscolhidos.add(it)
            }

            //Atualização das Etiquetas Escolhidas da Tarefa no Banco de Dados
            viewModelTarefa.atualizaEtiquetasEscolhidas(
                chipsEscolhidos,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )

            builder.dismiss()

        }
        view.botFechar.setOnClickListener {
            builder.dismiss()
        }
    }

    private fun setEtiquetas(list: ArrayList<String>) {
        list.forEach {
            criaEtiquetas(it, false)
        }
    }
    //Aqui termina o código de etiqueta

    private fun entradaChecklist() {
        inputCheckbox.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                checklist?.removeIf { i ->
                    i == ""
                }
                Log.i("inputCheckbox", inputCheckbox.getText().toString())
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

                return@setOnKeyListener true
            }
            false
        }
    }

    fun View.setMargins(
        left: Int = this.marginLeft,
        top: Int = this.marginTop,
        right: Int = this.marginRight,
        bottom: Int = this.marginBottom,
        start: Int = this.marginStart,
        end: Int = this.marginEnd
    ) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
            marginStart = start
            marginEnd = end
        }
    }

    private fun criaCheckbox(texto: String) {


        val layout_checkbox = RelativeLayout(this)
        linear_layout_checklist.addView(layout_checkbox as View)
        val checkbox = CheckBox(this)
        layout_checkbox.addView(checkbox as View)
        val botao_excluir_checkbox = ImageView(this)
        layout_checkbox.addView(botao_excluir_checkbox as View)

        layout_checkbox.apply {
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        }

        checkbox.apply {
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT
            setMargins(0, 0, 0, 0, 0, 50)
            text = texto
        }
        checkbox.isChecked = checklistConcluido?.contains(checkbox.text) == true

        val layoutParams = RelativeLayout.LayoutParams(
            50,
            50
        )
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        layoutParams.addRule(RelativeLayout.END_OF, checkbox.id)
        layoutParams.addRule(RelativeLayout.ALIGN_END, checkbox.id)
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        //layoutParams.bottomMargin = 10

        botao_excluir_checkbox.layoutParams = layoutParams
        //botao_excluir_checkbox.setMargins(0,0,0,5)
        botao_excluir_checkbox.setImageResource(R.drawable.ic_fechar)
        botao_excluir_checkbox.visibility = INVISIBLE
        if (editChecklist) {
            botao_excluir_checkbox.visibility = VISIBLE
        }

        Log.i("Novo checkbox", checkbox.text.toString())

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            checklistConcluido?.removeIf { i ->
                i == ""
            }
            if (isChecked) {
                checklistConcluido?.add(checkbox.text.toString())
            } else {
                checklistConcluido?.remove(checkbox.text.toString())
            }
            Log.i("ChecklistConcluido", checklistConcluido.toString())
            //Atualização do Checklist Concluído da Tarefa no Banco de Dados
            viewModelTarefa.atualizaChecklistConcluido(
                checklistConcluido!!,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )
        }

        botao_excluir_checkbox.setOnClickListener {
            checklist?.remove(checkbox.text.toString())

            //Atualização do Checklist da Tarefa no Banco de Dados
            viewModelTarefa.atualizaChecklist(
                checklist!!,
                tarefaAtual!!.tarefaId,
                tarefaAtual!!.disciplinaId
            )

            linear_layout.removeView(layout_checkbox)

            if (checklistConcluido?.contains(checkbox.text)==true){
                checklistConcluido?.remove(checkbox.text.toString())
                viewModelTarefa.atualizaChecklistConcluido(
                    checklistConcluido!!,
                    tarefaAtual!!.tarefaId,
                    tarefaAtual!!.disciplinaId
                )
            }

        }
    }

    private fun setCheckList(list: ArrayList<String>) {
        list.forEach {
            criaCheckbox(it)
        }
    }

    //Código para enviar e-mail para o professor
    private fun getContato(destinatario: String, assunto: String) {
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
        mIntent.putExtra(Intent.EXTRA_SUBJECT, assunto)
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