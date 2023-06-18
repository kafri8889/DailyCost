package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.login_register.UserLoginUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserRegisterUseCase

data class LoginRegisterUseCases(
    val userRegisterUseCase: UserRegisterUseCase,
    val userLoginUseCase: UserLoginUseCase
)