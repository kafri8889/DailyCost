package com.dcns.dailycost.domain.use_case.balance

import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.domain.repository.IBalanceRepository
import kotlinx.coroutines.flow.Flow

class GetLocalBalanceUseCase(
    private val balanceRepository: IBalanceRepository
) {

    operator fun invoke(): Flow<UserBalance> {
        return balanceRepository.getUserBalance
    }

}