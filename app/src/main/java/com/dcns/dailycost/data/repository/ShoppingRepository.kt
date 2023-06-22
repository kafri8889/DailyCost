package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.ShoppingHandler
import com.dcns.dailycost.data.model.remote.response.ShoppingResponse
import com.dcns.dailycost.domain.repository.IShoppingRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ShoppingRepository @Inject constructor(
    private val shoppingHandler: ShoppingHandler
): IShoppingRepository {

    override suspend fun shopping(body: RequestBody, token: String): Response<ShoppingResponse> {
        return shoppingHandler.shopping(body, token)
    }
}