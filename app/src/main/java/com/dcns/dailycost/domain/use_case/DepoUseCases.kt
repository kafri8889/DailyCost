package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.depo.EditDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.GetRemoteBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.UpdateLocalBalanceUseCase

data class DepoUseCases(
    val editDepoUseCase: EditDepoUseCase,
    val updateLocalBalanceUseCase: UpdateLocalBalanceUseCase,
    val getRemoteBalanceUseCase: GetRemoteBalanceUseCase,
    val getLocalBalanceUseCase: GetLocalBalanceUseCase
)
