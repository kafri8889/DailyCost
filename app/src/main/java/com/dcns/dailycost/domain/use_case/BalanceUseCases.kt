package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.balance.GetBalanceUseCase

data class BalanceUseCases(
    val getBalanceUseCase: GetBalanceUseCase
)
