package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserPreference
import kotlinx.coroutines.flow.Flow

interface IUserPreferenceRepository {

	val getUserPreference: Flow<UserPreference>

	suspend fun setLanguage(language: Language)
	suspend fun setSecureApp(secure: Boolean)
	suspend fun setIsNotFirstInstall(notFirst: Boolean)

}