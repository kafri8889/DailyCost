package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.remote.response.ExpenseResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import retrofit2.Response

class GetRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        userId: Int,
        token: String
    ): Response<ExpenseResponse> {
        return expenseRepository.getExpense(userId, token)
    }

}