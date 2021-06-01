package com.example.mochila.bancoDados

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disciplinastable")
data class DisciplinasEntity (
    @PrimaryKey(autoGenerate = false)
    val disciplinaId: String = "",
    var usuarioId: String = "",
    var nomeDisciplina: String = "",
    var nomeProfessor: String = "",
    var emailProfessor: String = ""
)
