package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import okhttp3.RequestBody
import retrofit2.Response

interface ExpenseHandler {

    suspend fun addExpense(
        body: RequestBody,
        token: String
    ): Response<AddExpenseResponse>

    suspend fun getExpense(
        userId: Int,
        token: String
    ): Response<GetExpenseResponse>

    suspend fun deleteExpense(
        body: RequestBody,
        token: String
    ): Response<DeleteResponse>

}