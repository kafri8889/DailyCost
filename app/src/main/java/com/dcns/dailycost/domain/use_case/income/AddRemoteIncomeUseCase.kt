package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.remote.response.IncomeResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import okhttp3.RequestBody
import retrofit2.Response

class AddRemoteIncomeUseCase(
    private val incomeRepository: IIncomeRepository
) {

    suspend operator fun invoke(
        body: RequestBody,
        token: String
    ): Response<IncomeResponse> {
        return incomeRepository.addIncome(body, token)
    }

}