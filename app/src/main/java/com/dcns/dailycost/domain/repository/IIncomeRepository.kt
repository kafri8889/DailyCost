package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.remote.response.IncomeResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IIncomeRepository {

    suspend fun addIncome(
        body: RequestBody,
        token: String
    ): Response<IncomeResponse>

    suspend fun getIncome(
        userId: Int,
        token: String
    ): Response<IncomeResponse>

}