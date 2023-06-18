package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.BalanceResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class BalanceResponse(
    val data: BalanceResponseData,
    val status: String
): IResponse