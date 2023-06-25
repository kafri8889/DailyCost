package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.IncomeResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class IncomeResponse(
    val data: IncomeResponseData,
    val message: String,
    val status: String
): IResponse