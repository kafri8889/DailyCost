package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.foundation.common.IResponse

data class ErrorResponse(
    val message: String,
    val status: String
): IResponse
