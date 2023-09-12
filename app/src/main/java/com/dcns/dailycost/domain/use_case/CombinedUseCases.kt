package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.combined.GetBalanceUseCase
import com.dcns.dailycost.domain.use_case.combined.GetRecentActivityUseCase

data class CombinedUseCases(
	val getRecentActivityUseCase: GetRecentActivityUseCase,
	val getBalanceUseCase: GetBalanceUseCase
)
