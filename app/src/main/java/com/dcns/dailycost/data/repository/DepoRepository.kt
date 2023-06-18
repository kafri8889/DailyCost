package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.DepoHandler
import com.dcns.dailycost.data.model.networking.response.DepoResponse
import com.dcns.dailycost.domain.repository.IDepoRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class DepoRepository @Inject constructor(
    private val depoHandler: DepoHandler
): IDepoRepository {

    override suspend fun editDepo(body: RequestBody): Response<DepoResponse> {
        return depoHandler.editDepo(body)
    }

    override suspend fun addDepo(body: RequestBody): Response<DepoResponse> {
        return depoHandler.addDepo(body)
    }

    override suspend fun topup(body: RequestBody): Response<DepoResponse> {
        return depoHandler.topup(body)
    }
}