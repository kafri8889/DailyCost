package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.DepoHandler
import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.domain.repository.IDepoRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class DepoRepository @Inject constructor(
    private val depoHandler: DepoHandler
): IDepoRepository {

    override suspend fun editDepo(body: RequestBody, token: String): Response<DepoResponse> {
        return depoHandler.editDepo(body, token)
    }

    @Deprecated("otomatis ditambahkan saat user register")
    override suspend fun addDepo(body: RequestBody, token: String): Response<DepoResponse> {
        return depoHandler.addDepo(body, token)
    }

    @Deprecated("gunakan fitur Income")
    override suspend fun topup(body: RequestBody, token: String): Response<DepoResponse> {
        return depoHandler.topup(body, token)
    }
}