package com.dcns.dailycost.domain.use_case.depo

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import retrofit2.Response

/**
 * Use case untuk mendapatkan saldo dari server
 */
class GetRemoteBalanceUseCase(
    private val balanceRepository: IBalanceRepository
) {

    suspend operator fun invoke(
        userId: Int,
        token: String
    ): Response<DepoResponse> {
        return balanceRepository.getRemoteBalance(userId, token)
    }

}