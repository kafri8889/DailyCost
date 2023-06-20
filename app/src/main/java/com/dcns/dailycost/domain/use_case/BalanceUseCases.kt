package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.balance.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.balance.GetRemoteBalanceUseCase

data class BalanceUseCases(
    val getRemoteBalanceUseCase: GetRemoteBalanceUseCase,
    val getLocalBalanceUseCase: GetLocalBalanceUseCase
)
