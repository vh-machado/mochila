package com.example.mochila.principal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mochila.R
import com.example.mochila.databinding.ActivityJanelinhaTarefaBinding
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_janelinha_tarefa.*

class janelinhaTarefa : AppCompatActivity() {
    private lateinit var binding: ActivityJanelinhaTarefaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJanelinhaTarefaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        entryChip()
        botSalvar.setOnClickListener {

            Toast.makeText(this,"Salvo com sucesso", Toast.LENGTH_SHORT ).show()
        }


        filterChips()

    }

    private fun entryChip(){
        binding.infoEtiqueta.setOnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){

                binding.apply {
                    val name = infoEtiqueta.text.toString()
                    creatChips(name)

                    infoEtiqueta.text!!.clear()
                }

                return@setOnKeyListener true
            }
            false
        }
    }

    private fun creatChips(name:String){
        val chip = Chip(this)
        chip.apply {
            text = name
            chipIcon = ContextCompat.getDrawable(
                this@janelinhaTarefa,
                 R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = true

            chip.chipBackgroundColor = getColorStateList(
                R.color.pale_cerulean
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
    /*
    private fun choiceChips(){
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip:Chip? = group.findViewById(checkedId)
            chip?.let {

            }

        }
    }

     */
    private fun filterChips(){
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            if (chip?.isChecked == true) {
                chip?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )

            }
            if (etiqueta1?.isChecked == true) {
                etiqueta1.isCheckedIconVisible = true
                etiqueta1.isCloseIconVisible = false
                etiqueta1?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else{
                etiqueta1?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta2?.isChecked == true) {
                etiqueta2.isCheckedIconVisible = true
                etiqueta2.isCloseIconVisible = false
                etiqueta2?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else if(etiqueta2?.isChecked == false) {
                etiqueta2?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta3?.isChecked == true) {
                etiqueta3.isCheckedIconVisible = true
                etiqueta3.isCloseIconVisible = false
                etiqueta3?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta3?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta4?.isChecked == true) {
                etiqueta4.isCheckedIconVisible = true
                etiqueta4.isCloseIconVisible = false
                etiqueta4?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta4?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta5?.isChecked == true) {
                etiqueta5.isCheckedIconVisible = true
                etiqueta5.isCloseIconVisible = false
                etiqueta5?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else{
                etiqueta5?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta6?.isChecked == true) {
                etiqueta6.isCheckedIconVisible = true
                etiqueta6.isCloseIconVisible = false
                etiqueta6?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta6?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta7?.isChecked == true) {
                etiqueta7.isCheckedIconVisible = true
                etiqueta7.isCloseIconVisible = false
                etiqueta7?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta7?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta8?.isChecked == true) {
                etiqueta8.isCheckedIconVisible = true
                etiqueta8.isCloseIconVisible = false
                etiqueta8?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta8?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
            if (etiqueta9?.isChecked == true) {
                etiqueta9.isCheckedIconVisible = true
                etiqueta9.isCloseIconVisible = false
                etiqueta9?.chipBackgroundColor = getColorStateList(
                    R.color.bdazzled_blue
                )
            } else {
                etiqueta9?.chipBackgroundColor = getColorStateList(
                    R.color.pale_cerulean
                )
            }
        }
    }
}