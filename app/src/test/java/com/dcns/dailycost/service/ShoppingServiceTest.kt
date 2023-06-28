package com.dcns.dailycost.service

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.services.ShoppingService
import com.dcns.dailycost.data.model.remote.request_body.ShoppingRequestBody
import com.dcns.dailycost.foundation.util.TestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShoppingServiceTest {

    private lateinit var shoppingService: ShoppingService

    @BeforeEach
    fun setUp() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        shoppingService = retrofit.create(ShoppingService::class.java)
    }

    @Test
    fun `post shopping`() = runTest {
        val reqBody = ShoppingRequestBody(
            userId = TestUtil.adminUserId,
            amount = 7_000_000,
            name = "GeForce RTX 3090",
            payment = "GOPAY",
            category = "Komputer",
            date = "2023-06-07"
        ).toRequestBody()

        shoppingService.shopping(reqBody, TestUtil.adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

}