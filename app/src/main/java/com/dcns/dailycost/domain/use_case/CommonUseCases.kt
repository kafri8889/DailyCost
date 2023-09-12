package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.common.CheckTokenExpiredUseCase
import com.dcns.dailycost.domain.use_case.common.GetBalanceUseCase
import com.dcns.dailycost.domain.use_case.common.GetRecentActivityUseCase

data class CommonUseCases(
	val checkTokenExpiredUseCase: CheckTokenExpiredUseCase,
	val getRecentActivityUseCase: GetRecentActivityUseCase,
	val getBalanceUseCase: GetBalanceUseCase
)
