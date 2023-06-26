package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.ExpenseResponse
import okhttp3.RequestBody
import retrofit2.Response

interface ExpenseHandler {

    suspend fun getExpense(
        userId: Int,
        token: String
    ): Response<ExpenseResponse>

    suspend fun deleteExpense(
        body: RequestBody,
        token: String
    ): Response<DeleteResponse>

}