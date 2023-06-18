package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.networking.response.ShoppingResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IShoppingRepository {

    suspend fun shopping(body: RequestBody): Response<ShoppingResponse>

}