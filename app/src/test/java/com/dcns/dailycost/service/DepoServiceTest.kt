package com.dcns.dailycost.service

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.services.DepoService
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.foundation.util.TestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DepoServiceTest {

    private lateinit var depoService: DepoService

    private val adminUserId = 73
    private val adminToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzMsImVtYWlsIjoiYWRtaW4iLCJpYXQiOjE2ODc2NzgwNDksImV4cCI6MTY4Nzc2NDQ0OX0.dKYzZY5hKbX4SfOlXICq3chgu2fVis_ezAdmG7BR7F0"

    @BeforeEach
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        depoService = retrofit.create(DepoService::class.java)
    }

    @Test
    fun `post depo`() = runTest {
        val reqBody = DepoRequestBody(
            id = adminUserId,
            cash = 1_000,
            eWallet = 1_000,
            bankAccount = 1_000
        ).toRequestBody()

        depoService.addDepo(reqBody, adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `edit depo`() = runTest {
        val reqBody = DepoRequestBody(
            id = adminUserId,
            cash = 15_000,
            eWallet = 1_000_000,
            bankAccount = 50_000
        ).toRequestBody()

        depoService.editDepo(reqBody, adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `get depo`() = runTest {
        depoService.getBalance(adminUserId, adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

}