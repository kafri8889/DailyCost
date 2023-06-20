package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IDepoRepository {

    suspend fun editDepo(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

    suspend fun addDepo(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

    suspend fun topup(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

}