package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.ExpenseDb
import kotlinx.coroutines.flow.Flow

interface ExpenseDao {
    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseDb>>

    @Query("SELECT * FROM expense_table WHERE pengeluaran_id = :id")
    fun getExpenseById(id: Int): Flow<ExpenseDb?>

    @Query("SELECT * FROM expense_table WHERE userId_expense =:id")
    fun getExpenseByUserId(id: Int): Flow<List<ExpenseDb>>

    @Update
    fun updateExpense(vararg note: ExpenseDb)

    @Upsert
    fun upsertExpense(vararg note: ExpenseDb)

    @Delete
    fun deleteExpense(vararg note: ExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(vararg note: ExpenseDb)
}