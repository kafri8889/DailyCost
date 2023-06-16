package com.dcns.dailycost.ui.login_register

import com.dcns.dailycost.ui.login_register.data.LoginRegisterType

data class LoginRegisterState(
    val loginRegisterType: LoginRegisterType = LoginRegisterType.Login,
    val showPassword: Boolean = false,
    val rememberMe: Boolean = false,
    val passwordError: String? = null,
    val emailError: String? = null,
    val passwordRe: String = "",
    val password: String = "",
    val username: String = "",
    val email: String = "",
)
