package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.BalanceResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class BalanceResponse(
    val data: BalanceResponseData,
    val status: String
): IResponse