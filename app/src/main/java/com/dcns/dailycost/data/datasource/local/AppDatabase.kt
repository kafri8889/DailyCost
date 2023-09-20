package com.dcns.dailycost.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dcns.dailycost.data.datasource.local.dao.CategoryDao
import com.dcns.dailycost.data.datasource.local.dao.ExpenseDao
import com.dcns.dailycost.data.datasource.local.dao.IncomeDao
import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.datasource.local.dao.NotificationDao
import com.dcns.dailycost.data.model.local.CategoryDb
import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.data.model.local.NoteDb
import com.dcns.dailycost.data.model.local.NotificationDb

/**
 * Database untuk aplikasi ini
 */
@Database(
	entities = [
		NoteDb::class,
		IncomeDb::class,
		ExpenseDb::class,
		CategoryDb::class,
		NotificationDb::class
	],
	version = 4,
	exportSchema = false
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

	abstract fun noteDao(): NoteDao
	abstract fun incomeDao(): IncomeDao
	abstract fun expenseDao(): ExpenseDao
	abstract fun categoryDao(): CategoryDao
	abstract fun notificationDao(): NotificationDao

	companion object {
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class) {
					INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
						.fallbackToDestructiveMigration()
						.build()
				}
			}

			return INSTANCE!!
		}
	}
}