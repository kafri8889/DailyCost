package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.LoginResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class LoginResponse(
    val data: LoginResponseData,
    val status: String,
    val token: String
): IResponse