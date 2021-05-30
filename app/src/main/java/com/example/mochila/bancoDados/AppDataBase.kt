package com.example.mochila.bancoDados

import android.content.Context
import androidx.room.*

@Database(
    entities = [UsersEntity::class,TarefaEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(ConverterArray::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usersDAO(): UsersDAO
    abstract fun tarefaDAO(): TarefasDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "AppDataBase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}