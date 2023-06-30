package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import okhttp3.RequestBody
import retrofit2.Response

/**
 * Use case untuk menambahkan pengeluaran ke database server
 */
class AddRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        body: RequestBody,
        token: String
    ): Response<AddExpenseResponse> {
        return expenseRepository.addRemoteExpense(body, token)
    }

}