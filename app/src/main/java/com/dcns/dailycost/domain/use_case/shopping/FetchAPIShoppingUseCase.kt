package com.dcns.dailycost.domain.use_case.shopping

import com.dcns.dailycost.data.model.networking.response.ShoppingResponse
import com.dcns.dailycost.domain.repository.IShoppingRepository
import okhttp3.RequestBody
import retrofit2.Response

class FetchAPIShoppingUseCase(
    private val shoppingRepository: IShoppingRepository
) {

    suspend operator fun invoke(
        body: RequestBody
    ): Response<ShoppingResponse> {
        return shoppingRepository.shopping(body)
    }

}