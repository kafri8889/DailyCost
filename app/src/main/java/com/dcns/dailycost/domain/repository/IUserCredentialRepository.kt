package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.UserCredential
import kotlinx.coroutines.flow.Flow

interface IUserCredentialRepository {

	val getUserCredential: Flow<UserCredential>

	suspend fun setId(id: String)

	suspend fun setName(name: String)

	suspend fun setEmail(email: String)

	suspend fun setToken(token: String)

	suspend fun setPassword(password: String)

}