package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.ExpenseHandler
import com.dcns.dailycost.data.datasource.remote.services.ExpenseService
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.ExpenseResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ExpenseHandlerImpl @Inject constructor(
    private val expenseService: ExpenseService
): ExpenseHandler {

    override suspend fun getExpense(userId: Int, token: String): Response<ExpenseResponse> {
        return expenseService.getExpense(userId, token)
    }

    override suspend fun deleteExpense(body: RequestBody, token: String): Response<DeleteResponse> {
        return expenseService.deleteExpense(body, token)
    }

}