package com.dcns.dailycost.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dcns.dailycost.data.datasource.local.dao.ExpenseDao
import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.local.NoteDb

@Database(
    entities = [
        NoteDb::class,
        ExpenseDb::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app.db"
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }
}