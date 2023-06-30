package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import retrofit2.Response

/**
 * Use case untuk mendapatkan pengeluaran dari database server
 */
class GetRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        userId: Int,
        token: String
    ): Response<GetExpenseResponse> {
        return expenseRepository.getRemoteExpense(userId, token)
    }

}