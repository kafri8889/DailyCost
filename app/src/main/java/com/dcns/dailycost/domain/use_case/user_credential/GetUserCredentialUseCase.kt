package com.dcns.dailycost.domain.use_case.user_credential

import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case untuk mendapatkan user credential
 */
class GetUserCredentialUseCase(
	private val userCredentialRepository: IUserCredentialRepository
) {

	operator fun invoke(): Flow<UserCredential> {
		return userCredentialRepository.getUserCredential
	}

}