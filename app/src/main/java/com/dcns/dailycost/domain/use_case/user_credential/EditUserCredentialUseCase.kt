package com.dcns.dailycost.domain.use_case.user_credential

import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.util.EditUserCredentialType

/**
 * Use case untuk mengedit user credential
 */
class EditUserCredentialUseCase(
    private val userCredentialRepository: IUserCredentialRepository
) {

    suspend operator fun invoke(
        type: EditUserCredentialType
    ) {
        when (type) {
            is EditUserCredentialType.Email -> userCredentialRepository.setEmail(type.value)
            is EditUserCredentialType.ID -> userCredentialRepository.setId(type.value)
            is EditUserCredentialType.Name -> userCredentialRepository.setName(type.value)
            is EditUserCredentialType.Password -> userCredentialRepository.setPassword(type.value)
            is EditUserCredentialType.Token -> userCredentialRepository.setToken(type.value)
        }
    }

}