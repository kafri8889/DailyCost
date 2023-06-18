package com.dcns.dailycost.domain.use_case.balance

import com.dcns.dailycost.data.model.remote.response.BalanceResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import retrofit2.Response

class GetBalanceUseCase(
    private val balanceRepository: IBalanceRepository
) {

    suspend operator fun invoke(
        userId: Int,
        token: String
    ): Response<BalanceResponse> {
        return balanceRepository.getBalance(userId, token)
    }

}