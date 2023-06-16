package com.dcns.dailycost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.foundation.extension.toUserCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserCredentialRepository @Inject constructor(
    private val userCredentialDataStore: DataStore<ProtoUserCredential>
): IUserCredentialRepository {

    override val getUserCredential: Flow<UserCredential>
        get() = userCredentialDataStore.data
            .map { it.toUserCredential() }

    override suspend fun setName(name: String) {
        userCredentialDataStore.updateData { cred ->
            cred.copy(
                name = name
            )
        }
    }

    override suspend fun setEmail(email: String) {
        userCredentialDataStore.updateData { cred ->
            cred.copy(
                email = email
            )
        }
    }

    override suspend fun setPassword(password: String) {
        userCredentialDataStore.updateData { cred ->
            cred.copy(
                password = password
            )
        }
    }

    companion object {
        val corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { ProtoUserCredential() }
        )
    }
}