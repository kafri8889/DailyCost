package com.dcns.dailycost.ui.login

import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.foundation.common.IResponse

data class LoginState(
    val internetConnectionAvailable: Boolean = true,
    val resource: Resource<IResponse?>? = null,
    val showPassword: Boolean = false,
    val rememberMe: Boolean = false,
    val passwordError: String? = null,
    val emailError: String? = null,
    val password: String = "",
    val email: String = "",
)
