package com.dcns.dailycost.data.model.remote.response.expense

import com.dcns.dailycost.data.model.remote.GetExpenseResponseData
import com.dcns.dailycost.foundation.common.IResponse
import com.google.gson.annotations.SerializedName

data class GetExpenseResponse(
	@SerializedName("message") val message: String,
	@SerializedName("data") val data: GetExpenseResponseData,
	@SerializedName("status") val status: String
): IResponse