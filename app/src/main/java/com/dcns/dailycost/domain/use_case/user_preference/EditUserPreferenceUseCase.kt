package com.dcns.dailycost.domain.use_case.user_preference

import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import com.dcns.dailycost.domain.util.EditUserPreferenceType

/**
 * Use case untuk mengedit user credential
 */
class EditUserPreferenceUseCase(
	private val userPreferenceRepository: IUserPreferenceRepository
) {

	suspend operator fun invoke(
		type: EditUserPreferenceType
	) {
		when (type) {
			is EditUserPreferenceType.Language -> userPreferenceRepository.setLanguage(type.value)
			is EditUserPreferenceType.SecureApp -> userPreferenceRepository.setSecureApp(type.value)
			is EditUserPreferenceType.IsNotFirstInstall -> userPreferenceRepository.setIsNotFirstInstall(
				type.value
			)
		}
	}

}
