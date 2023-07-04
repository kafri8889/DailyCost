package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import okhttp3.RequestBody
import retrofit2.Response

interface ExpenseHandler {

    /**
     * Tambahkan pengeluaran baru
     */
    suspend fun addExpense(
        userId: Int,
        body: RequestBody,
        token: String
    ): Response<AddExpenseResponse>

    /**
     * Dapatkan pengeluaran dengan user id
     */
    suspend fun getExpense(
        userId: Int,
        token: String
    ): Response<GetExpenseResponse>

    /**
     * Hapus pengeluaran
     */
    suspend fun deleteExpense(
        userId: Int,
        body: RequestBody,
        token: String
    ): Response<DeleteResponse>

}