package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.ExpenseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response

interface IExpenseRepository {

    suspend fun getRemoteExpense(
        userId: Int,
        token: String
    ): Response<ExpenseResponse>

    suspend fun deleteRemoteExpense(
        body: RequestBody,
        token: String
    ): Response<DeleteResponse>

    fun getAllExpenses(): Flow<List<ExpenseDb>>

    fun getExpenseById(id: Int): Flow<ExpenseDb?>

    suspend fun updateExpense(vararg expense: ExpenseDb)

    suspend fun upsertExpense(vararg expense: ExpenseDb)

    suspend fun deleteExpense(vararg expense: ExpenseDb)

    suspend fun insertExpense(vararg expense: ExpenseDb)

}