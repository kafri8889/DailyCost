package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.RegisterResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class RegisterResponse(
    val data: RegisterResponseData,
    val message: String,
    val token: String
): IResponse
