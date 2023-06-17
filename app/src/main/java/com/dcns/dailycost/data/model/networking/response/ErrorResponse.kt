package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.foundation.common.IResponse

data class ErrorResponse(
    val message: String
): IResponse
