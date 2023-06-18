package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.ShoppingResponse
import okhttp3.RequestBody
import retrofit2.Response

interface ShoppingHandler {

    suspend fun shopping(body: RequestBody): Response<ShoppingResponse>

}