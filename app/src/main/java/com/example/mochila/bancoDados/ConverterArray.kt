package com.example.mochila.bancoDados

import androidx.room.TypeConverter
import java.util.ArrayList

// Converter o array

class ConverterArray {
    @TypeConverter
    fun fromArray(string: ArrayList<String?>?): String {
        var string = ""
        for (s in string) string += ","
        return string
    }

    @TypeConverter
    fun toArray(concatenatedString: String?): ArrayList<String> {
       val myStrings = ArrayList<String>()

        for (s in concatenatedString!!.split(",")) {
            myStrings.add(s)

        }
       return myStrings
    }
}