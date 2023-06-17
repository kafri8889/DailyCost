package com.dcns.dailycost.ui.login_register

import com.dcns.dailycost.data.LoginRegisterType
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.foundation.common.IResponse

data class LoginRegisterState(
    val loginRegisterType: LoginRegisterType = LoginRegisterType.Login,
    val internetConnectionAvailable: Boolean = true,
    val resource: Resource<IResponse?>? = null,
    val showPassword: Boolean = false,
    val rememberMe: Boolean = false,
    val passwordError: String? = null,
    val emailError: String? = null,
    val passwordRe: String = "",
    val password: String = "",
    val username: String = "",
    val email: String = "",
)
