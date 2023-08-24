package com.dcns.dailycost.domain.use_case.user_preference

import com.dcns.dailycost.data.model.UserPreference
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case untuk mendapatkan user preference
 */
class GetUserPreferenceUseCase(
	private val userPreferenceRepository: IUserPreferenceRepository
) {

	operator fun invoke(): Flow<UserPreference> {
		return userPreferenceRepository.getUserPreference
	}

}