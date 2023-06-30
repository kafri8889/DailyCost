package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.data.model.local.relation.IncomeDbWithCategoryDb
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response

interface IIncomeRepository {

    /**
     * Tambah pemasukan baru ke database server
     */
    suspend fun addRemoteIncome(
        body: RequestBody,
        token: String
    ): Response<IncomePostResponse>

    /**
     * Dapatkan pemasukan dari id yang diberikan dari database server
     */
    suspend fun getRemoteIncome(
        userId: Int,
        token: String
    ): Response<IncomeGetResponse>

    /**
     * Dapatkan semua pemasukan dari database lokal
     */
    fun getAllIncomes(): Flow<List<IncomeDbWithCategoryDb>>

    /**
     * Dapatkan pemasukan by id dari database lokal
     */
    fun getIncomeById(id: Int): Flow<IncomeDbWithCategoryDb?>

    /**
     * Update pemasukan ke database lokal
     */
    suspend fun updateIncome(vararg income: IncomeDb)

    /**
     * Update atau insert pemasukan ke database lokal
     */
    suspend fun upsertIncome(vararg income: IncomeDb)

    /**
     * Hapus pemasukan dari database lokal
     */
    suspend fun deleteIncome(vararg income: IncomeDb)

    /**
     * Insert pemasukan ke database lokal
     */
    suspend fun insertIncome(vararg income: IncomeDb)

}