package com.dcns.dailycost.service

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.services.IncomeService
import com.dcns.dailycost.data.model.remote.request_body.IncomeRequestBody
import com.dcns.dailycost.foundation.util.TestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IncomeServiceTest {

    private lateinit var incomeService: IncomeService

    private val adminUserId = 73
    private val adminToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzMsImVtYWlsIjoiYWRtaW4iLCJpYXQiOjE2ODc2NzgwNDksImV4cCI6MTY4Nzc2NDQ0OX0.dKYzZY5hKbX4SfOlXICq3chgu2fVis_ezAdmG7BR7F0"

    @BeforeEach
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        incomeService = retrofit.create(IncomeService::class.java)
    }

    @Test
    fun `post income`() = runTest {
        val reqBody = IncomeRequestBody(
            amount = 50_000,
            category = "Makanan",
            name = "Kimbap",
            payment = "CASH",
            date = "2023-05-01",
            userId = adminUserId
        ).toRequestBody()

        incomeService.addIncome(reqBody, adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `get income`() = runTest {
        incomeService.getIncome(adminUserId, adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

}