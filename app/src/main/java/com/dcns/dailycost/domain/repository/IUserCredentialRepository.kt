package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.UserCredential
import kotlinx.coroutines.flow.Flow

interface IUserCredentialRepository {

    val getUserCredential: Flow<UserCredential>

    suspend fun setName(name: String)

    suspend fun setEmail(email: String)

    suspend fun setPassword(password: String)

}