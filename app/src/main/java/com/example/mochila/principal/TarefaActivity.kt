package com.example.mochila.principal

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.example.mochila.R
import com.example.mochila.bancoDados.TarefaEntity
import com.example.mochila.databinding.ActivityTarefaBinding
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.view.*

import kotlinx.android.synthetic.main.activity_tarefa.*
import java.lang.Exception
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

        val chips = intent.getSerializableExtra("chips")as? ArrayList<String>
        val tarefaSelecionada = intent.getSerializableExtra("tarefa")as?TarefaEntity

        var listaChips = arrayListOf<String>()
        chips?.forEach {
            listaChips.add(it)
        }
        setDefaultChips(listaChips)
        Log.i("chips na tarefa:",chips.toString())

        var foraChips = arrayListOf<String>()
        chips?.forEach {
            foraChips.remove(it)
        }
        setDefaultChips(foraChips)

        botAdicionarEtiqueta.setOnClickListener {
         val intent = Intent(this, janelinhaTarefa::class.java)
            startActivity(intent)
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

    private fun pickDate(){
        textDate.setOnClickListener{
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
        builder.setPositiveButton("Sim"){
            dialog, which ->
            chipGroupTarefa.removeView(chip)
            Toast.makeText(this,"Etiqueta removida", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar"){
                dialog, which ->
            dialog.dismiss()

        }
        builder.create().show()

    }
    //ARMAZENAR

    private  fun setDefaultChips(list: List <String>){
        list.forEach{
            creatChips(it,false)
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