package com.dcns.dailycost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.dcns.dailycost.ProtoUserPreference
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserPreference
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import com.dcns.dailycost.foundation.extension.toUserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
    private val userPreferenceDataStore: DataStore<ProtoUserPreference>
): IUserPreferenceRepository {

    override val getUserPreference: Flow<UserPreference>
        get() = userPreferenceDataStore.data
            .map { it.toUserPreference() }

    override suspend fun setLanguage(language: Language) {
        userPreferenceDataStore.updateData {
            it.copy(
                language = language.ordinal
            )
        }
    }

    override suspend fun setSecureApp(secure: Boolean) {
        userPreferenceDataStore.updateData {
            it.copy(
                secureApp = secure
            )
        }
    }

    companion object {
        val corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { ProtoUserPreference() }
        )
    }
}