package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import okhttp3.RequestBody
import retrofit2.Response

/**
 * Use case untuk menghapus pemasukan dari database server
 */
class DeleteRemoteIncomeUseCase(
    private val incomeRepository: IIncomeRepository
) {

    suspend operator fun invoke(
        userId: Int,
        body: RequestBody,
        token: String
    ): Response<DeleteResponse> {
        return incomeRepository.deleteRemoteIncome(userId, body, token)
    }

}