package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.ShoppingResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class ShoppingResponse(
    val data: ShoppingResponseData,
    val message: String,
    val status: String
): IResponse