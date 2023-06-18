package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.DepoResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class DepoResponse(
    val data: DepoResponseData,
    val message: String,
    val status: String
): IResponse