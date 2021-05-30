package com.example.mochila.bancoDados

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disciplinastable")
data class DisciplinasEntity (
    @PrimaryKey
    val id: String = "",
    var nome: String = "",
    var nomeProfessor: String = "",
    var emailProfessor: String = ""
)
