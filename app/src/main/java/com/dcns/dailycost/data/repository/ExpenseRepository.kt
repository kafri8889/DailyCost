package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.ExpenseHandler
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.ExpenseResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseHandler: ExpenseHandler
): IExpenseRepository {

    override suspend fun getExpense(userId: Int, token: String): Response<ExpenseResponse> {
        return expenseHandler.getExpense(userId, token)
    }

    override suspend fun deleteExpense(body: RequestBody, token: String): Response<DeleteResponse> {
        return expenseHandler.deleteExpense(body, token)
    }

}