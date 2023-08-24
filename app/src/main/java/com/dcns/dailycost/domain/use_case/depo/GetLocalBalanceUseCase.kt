package com.dcns.dailycost.domain.use_case.depo

import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.domain.repository.IBalanceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case untuk mendapatkan saldo lokal
 */
class GetLocalBalanceUseCase(
	private val balanceRepository: IBalanceRepository
) {

	operator fun invoke(): Flow<UserBalance> {
		return balanceRepository.getUserBalance
	}

}