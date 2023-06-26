package com.dcns.dailycost.service

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.services.ExpenseService
import com.dcns.dailycost.data.model.remote.request_body.DeleteExpenseRequestBody
import com.dcns.dailycost.foundation.util.TestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExpenseServiceTest {

    private lateinit var expenseService: ExpenseService

    @BeforeEach
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        expenseService = retrofit.create(ExpenseService::class.java)
    }

    @Test
    fun `get expense`() = runTest {
        expenseService.getExpense(TestUtil.adminUserId, TestUtil.adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `delete expense`() = runTest {

        // Kalo mau test delete expense
        // Pastiin [expenseId] nya ada di server!
        val reqBody = DeleteExpenseRequestBody(
            expenseId = 146,
            userId = TestUtil.adminUserId
        ).toRequestBody()

        expenseService.deleteExpense(reqBody, TestUtil.adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

}