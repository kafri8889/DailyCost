package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.local.relation.ExpenseDbWithCategoryDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Transaction
    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseDbWithCategoryDb>>

    @Transaction
    @Query("SELECT * FROM expense_table WHERE id_expense = :id")
    fun getExpenseById(id: Int): Flow<ExpenseDbWithCategoryDb?>

    @Update
    suspend fun updateExpense(vararg expense: ExpenseDb)

    @Upsert
    suspend fun upsertExpense(vararg expense: ExpenseDb)

    @Delete
    suspend fun deleteExpense(vararg expense: ExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(vararg expense: ExpenseDb)
}