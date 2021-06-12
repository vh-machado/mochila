package com.example.mochila.bancoDados

import android.content.Context
import androidx.room.*

@Database(
    entities = [UsersEntity::class,TarefaEntity::class,DisciplinasEntity::class],
    version = 14,
    exportSchema = false
)
@TypeConverters(ConverterArray::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usersDAO(): UsersDAO
    abstract fun tarefaDAO(): TarefasDAO
    abstract fun disciplinasDAO(): DisciplinasDAO

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