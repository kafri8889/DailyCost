package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.user_credential.EditUserCredentialUseCase
import com.dcns.dailycost.domain.use_case.user_credential.GetUserCredentialUseCase

data class UserCredentialUseCases(
    val editUserCredentialUseCase: EditUserCredentialUseCase,
    val getUserCredentialUseCase: GetUserCredentialUseCase
)
