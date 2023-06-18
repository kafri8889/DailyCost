package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.depo.AddDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.EditDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.TopUpDepoUseCase

data class DepoUseCases(
    val editDepoUseCase: EditDepoUseCase,
    val addDepoUseCase: AddDepoUseCase,
    val topUpDepoUseCase: TopUpDepoUseCase
)
