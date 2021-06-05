package com.example.mochila.principal

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import androidx.lifecycle.ViewModelProvider
import com.example.mochila.R
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
        viewModelTarefa = ViewModelProvider(this).get(TarefaViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        // Código de etiquetas
        binding = ActivityTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var chips = intent.getSerializableExtra("chips")as? ArrayList<String>
        val tarefaSelecionada = intent.getSerializableExtra("tarefa")as?TarefaEntity
        if(tarefaSelecionada != null){
            multilineDescricao.setText(tarefaSelecionada!!.descricao)
            textDate.setText(tarefaSelecionada.dataEntrega)
            chips = tarefaSelecionada.etiquetas
        }

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

    private fun getDateCalendar(){
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

    //ARMAZENAR
    override fun onDateSet(view: DatePicker?, ano: Int, mes: Int, diaDoMes: Int){
        diaSalvo = String.format("%02d", diaDoMes)
        mesSalvo = String.format("%02d", mes)
        anoSalvo = ano.toString()
        var data = "$diaSalvo/$mesSalvo/$anoSalvo"
        textDate.setText(data)
    }

    // ARMAZENAR
    private  fun creatChips(name: String, closeIconStatus: Boolean){
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
    // ARMAZENAR
    fun creatAlertDialog(chip:Chip){
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
        builder.show()
        setDefaultChips(setListChips())
        entryChip()

        botSalvar.setOnClickListener {
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            shareInfosChipsIntent(getOnListIdChips())

        }
        botFechar.setOnClickListener {
            builder.dismiss()
        }
    }
    private fun entryChip() {

        infoEtiqueta.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.apply {
                    val name = infoEtiqueta.text.toString()
                    if(campoVazio()!= true){
                        creatChips(name, true)
                        infoEtiqueta.text!!.clear()
                    }
                }

                return@setOnKeyListener true
            }
            false
        }

    }

    private fun creatChips1(name: String, closeIconStatus: Boolean) {
        val chip = Chip(this)
        chip.apply {
            cout = cout + 1
            text = name
            chipIcon = ContextCompat.getDrawable(
                this@TarefaActivity,
                R.drawable.ic_launcher_background
            )
            id = cout
            isChipIconVisible = false
            isCloseIconVisible = closeIconStatus
            isClickable = true
            isCheckable = true

            chip.chipBackgroundColor = getColorStateList(
                R.color.bdazzled_blue
            )

            chip.setTextColor(getResources().getColor(R.color.white))

            binding.apply {
                chipGroup.addView(chip as View)

                chip.setOnCloseIconClickListener {
                    chipGroup.removeView(chip as View)
                }
            }

        }
    }

    private fun setDefaultChips(list: List<String>) {
        list.forEach {
            creatChips1(it, false)

        }
    }

    private fun setListChips(): List<String> {
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


    private fun getOnListIdChips():ArrayList<String> {
        var etiCheck = ArrayList<String>()
        chipGroup.checkedChipIds.forEach {
            var chip = chipGroup.findViewById<Chip>(it)
            etiCheck.add(chip.text.toString())

        }
        return etiCheck

    }

    private fun shareInfosChipsIntent(chips: ArrayList<String>){
        val intent = Intent(this, TarefaActivity:: class.java)
        intent.putExtra("chips",chips)
        startActivity(intent)
        finish()
    }
    private fun campoVazio():Boolean{
        val eti = infoEtiqueta.text.toString()
        if(eti.isNullOrBlank()== true){
            Toast.makeText(this,"Para criar etiquetas é necessário informar um nome! Verifique se o campo foi preenchido", Toast.LENGTH_SHORT).show()
        }
        return true
    }
        //ARMAZENAR

    private fun setDefaultChips1(list: List<String>) {
        list.forEach {
            creatChips(it, false)
        }
    }
    //Aqui termina o código de etiqueta

    //Código para enviar e-mail para o professor
    private fun getContato(destinatario: String){
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

        } catch (e: Exception){
            // if any thing goes wrong for example no email client application or any exception
            // get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

        }
    }

}