package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IDepoRepository {

    /**
     * edit user balance
     */
    suspend fun editDepo(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

    @Deprecated("otomatis ditambahkan saat user register")
    suspend fun addDepo(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

    @Deprecated("gunakan fitur Income")
    suspend fun topup(
        body: RequestBody,
        token: String
    ): Response<DepoResponse>

}