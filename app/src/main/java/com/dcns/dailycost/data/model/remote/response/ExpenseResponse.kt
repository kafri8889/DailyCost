package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.ExpenseResponseData
import com.dcns.dailycost.foundation.common.IResponse
import com.google.gson.annotations.SerializedName

data class ExpenseResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ExpenseResponseData,
    @SerializedName("status") val status: String
): IResponse