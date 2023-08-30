package com.dcns.dailycost.ui.register

import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.foundation.common.IResponse

data class RegisterState(
	val internetConnectionAvailable: Boolean = true,
	val resource: Resource<IResponse?>? = null,
	val showPassword: Boolean = false,
	val passwordError: String? = null,
	val usernameError: String? = null,
	val emailError: String? = null,
	val password: String = "",
	val email: String = "",
	val username: String = "",
)
