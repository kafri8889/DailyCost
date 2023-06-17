package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.LoginResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class LoginResponse(
    val data: LoginResponseData,
    val status: String,
    val token: String
): IResponse