package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import retrofit2.Response

class GetRemoteIncomeUseCase(
    private val incomeRepository: IIncomeRepository
) {

    suspend operator fun invoke(
        userId: Int,
        token: String
    ): Response<IncomeGetResponse> {
        return incomeRepository.getIncome(userId, token)
    }

}