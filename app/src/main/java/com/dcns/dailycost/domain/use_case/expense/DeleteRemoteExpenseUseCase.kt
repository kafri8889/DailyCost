package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import okhttp3.RequestBody
import retrofit2.Response

/**
 * Use case untuk menghapus pengeluaran dari database server
 */
class DeleteRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        body: RequestBody,
        token: String
    ): Response<DeleteResponse> {
        return expenseRepository.deleteRemoteExpense(body, token)
    }

}