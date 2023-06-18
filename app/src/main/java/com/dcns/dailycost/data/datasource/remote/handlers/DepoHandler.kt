package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response

interface DepoHandler {

    suspend fun editDepo(
        body: RequestBody
    ): Response<DepoResponse>

    suspend fun addDepo(
        body: RequestBody
    ): Response<DepoResponse>

    suspend fun topup(
        body: RequestBody
    ): Response<DepoResponse>

}