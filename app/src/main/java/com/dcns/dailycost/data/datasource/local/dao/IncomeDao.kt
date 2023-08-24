package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.data.model.local.relation.IncomeDbWithCategoryDb
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

	@Transaction
	@Query("SELECT * FROM income_table")
	fun getAllIncomes(): Flow<List<IncomeDbWithCategoryDb>>

	@Transaction
	@Query("SELECT * FROM income_table WHERE id_income = :id")
	fun getIncomeById(id: Int): Flow<IncomeDbWithCategoryDb?>

	@Query("DELETE FROM income_table WHERE id_income NOT IN (:ids)")
	suspend fun deleteIncomeExcept(ids: List<Int>)

	@Update
	suspend fun updateIncome(vararg income: IncomeDb)

	@Upsert
	suspend fun upsertIncome(vararg income: IncomeDb)

	@Delete
	suspend fun deleteIncome(vararg income: IncomeDb)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertIncome(vararg income: IncomeDb)

}