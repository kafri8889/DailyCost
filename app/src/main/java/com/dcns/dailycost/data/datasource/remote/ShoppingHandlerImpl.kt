package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.ShoppingHandler
import com.dcns.dailycost.data.datasource.remote.services.ShoppingService
import com.dcns.dailycost.data.model.remote.response.ShoppingResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ShoppingHandlerImpl @Inject constructor(
    private val shoppingService: ShoppingService
): ShoppingHandler {

    override suspend fun shopping(body: RequestBody, token: String): Response<ShoppingResponse> {
        return shoppingService.shopping(body, token)
    }
}