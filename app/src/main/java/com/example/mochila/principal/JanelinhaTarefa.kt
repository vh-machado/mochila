package com.example.mochila.principal


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mochila.R
import com.example.mochila.databinding.ActivityJanelinhaTarefaBinding
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*

class janelinhaTarefa : AppCompatActivity() {

    private lateinit var binding: ActivityJanelinhaTarefaBinding
    var cout = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJanelinhaTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultChips(setListChips())
        entryChip()

        botSalvar.setOnClickListener {
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            shareInfosChipsIntent(getOnListIdChips())

        }
        botFechar.setOnClickListener {
            finish()
        }

    }

    private fun entryChip() {
        binding.infoEtiqueta.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.apply {
                    val name = infoEtiqueta.text.toString()
                    creatChips(name, true)

                    infoEtiqueta.text!!.clear()

                }

                return@setOnKeyListener true
            }
            false
        }

    }

    private fun creatChips(name: String, closeIconStatus: Boolean) {
        val chip = Chip(this)
        chip.apply {
            cout = cout + 1
            text = name
            chipIcon = ContextCompat.getDrawable(
                this@janelinhaTarefa,
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
            creatChips(it, false)

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

}




