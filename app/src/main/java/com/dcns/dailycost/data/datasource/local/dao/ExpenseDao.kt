package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.ExpenseDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseDb>>

    @Query("SELECT * FROM expense_table WHERE id_expense = :id")
    fun getExpenseById(id: Int): Flow<ExpenseDb?>

    @Update
    fun updateExpense(vararg expense: ExpenseDb)

    @Upsert
    fun upsertExpense(vararg expense: ExpenseDb)

    @Delete
    fun deleteExpense(vararg expense: ExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(vararg expense: ExpenseDb)
}