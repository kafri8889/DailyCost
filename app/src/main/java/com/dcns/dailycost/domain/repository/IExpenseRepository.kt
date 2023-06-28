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

    suspend fun addRemoteExpense(
        body: RequestBody,
        token: String
    ): Response<AddExpenseResponse>

    suspend fun getRemoteExpense(
        userId: Int,
        token: String
    ): Response<GetExpenseResponse>

    suspend fun deleteRemoteExpense(
        body: RequestBody,
        token: String
    ): Response<DeleteResponse>

    fun getAllExpenses(): Flow<List<ExpenseDbWithCategoryDb>>

    fun getExpenseById(id: Int): Flow<ExpenseDbWithCategoryDb?>

    suspend fun deleteExpenseExcept(ids: List<Int>)

    suspend fun updateExpense(vararg expense: ExpenseDb)

    suspend fun upsertExpense(vararg expense: ExpenseDb)

    suspend fun deleteExpense(vararg expense: ExpenseDb)

    suspend fun insertExpense(vararg expense: ExpenseDb)

}