package com.example.mochila.bancoDados

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userstable")
data class UsersEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var nome: String,
    var email: String,
    var instituicao: String,
    var curso: String
)