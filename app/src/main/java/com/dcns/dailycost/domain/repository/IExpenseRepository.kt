package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.local.relation.ExpenseDbWithCategoryDb
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response

interface IExpenseRepository {

	/**
	 * Tambahkan pengeluaran baru ke database server
	 */
	suspend fun addRemoteExpense(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<AddExpenseResponse>

	/**
	 * Dapatkan pengeluaran dengan user id dari database server
	 */
	suspend fun getRemoteExpense(
		userId: Int,
		token: String
	): Response<GetExpenseResponse>

	/**
	 * Hapus pengeluaran dari database server
	 */
	suspend fun deleteRemoteExpense(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<DeleteResponse>

	/**
	 * Dapatkan semua pengeluaran dari database lokal
	 */
	fun getAllExpenses(): Flow<List<ExpenseDbWithCategoryDb>>

	/**
	 * Dapatkan pengeluaran by id dari database lokal
	 */
	fun getExpenseById(id: Int): Flow<ExpenseDbWithCategoryDb?>

	/**
	 * Hapus semua pengeluaran dari database lokal, kecuali dari id yang diberikan
	 * @param ids id pengeluaran yg dikecualikan
	 */
	suspend fun deleteExpenseExcept(ids: List<Int>)

	/**
	 * Update pengeluaran ke database lokal
	 */
	suspend fun updateExpense(vararg expense: ExpenseDb)

	/**
	 * Update atau insert pengeluaran ke database lokal
	 */
	suspend fun upsertExpense(vararg expense: ExpenseDb)

	/**
	 * Hapus pengeluaran dari database lokal
	 */
	suspend fun deleteExpense(vararg expense: ExpenseDb)

	/**
	 * Insert pengeluaran ke database lokal
	 */
	suspend fun insertExpense(vararg expense: ExpenseDb)

}