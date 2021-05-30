package com.example.mochila.bancoDados

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Update
import java.io.Serializable

@Entity(tableName = "userstable")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    var nome: String = "",
    var email: String = "",
    var disciplinas: String = ""
)

@Entity
class UsersUpdate(
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "disciplinas") private var disciplinas: String = ""
)





