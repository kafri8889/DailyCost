package com.dcns.dailycost.domain.use_case.login_register

data class LoginRegisterUseCases(
    val userRegisterUseCase: UserRegisterUseCase,
    val userLoginUseCase: UserLoginUseCase
)