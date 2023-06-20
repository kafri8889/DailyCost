package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.balance.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.balance.GetRemoteBalanceUseCase
import com.dcns.dailycost.domain.use_case.balance.UpdateLocalBalanceUseCase

data class BalanceUseCases(
    val updateLocalBalanceUseCase: UpdateLocalBalanceUseCase,
    val getRemoteBalanceUseCase: GetRemoteBalanceUseCase,
    val getLocalBalanceUseCase: GetLocalBalanceUseCase
)
