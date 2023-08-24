package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.user_preference.EditUserPreferenceUseCase
import com.dcns.dailycost.domain.use_case.user_preference.GetUserPreferenceUseCase

data class UserPreferenceUseCases(
	val editUserPreferenceUseCase: EditUserPreferenceUseCase,
	val getUserPreferenceUseCase: GetUserPreferenceUseCase
)