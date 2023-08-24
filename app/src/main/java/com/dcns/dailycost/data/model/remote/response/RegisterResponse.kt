package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.RegisterResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class RegisterResponse(
	val data: RegisterResponseData,
	val message: String,
	val token: String
): IResponse
