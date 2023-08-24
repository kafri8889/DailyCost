package com.dcns.dailycost.data.model.remote.request_body.income

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class DeleteIncomeRequestBody(
	@SerializedName("pemasukan_id") val incomeId: Int,
	@SerializedName("user_id") val userId: Int
): RetrofitRequestBody() {

	override fun getBody(): Map<String, Any> {
		return mapOf(
			"pemasukan_id" to incomeId,
			"user_id" to userId
		)
	}

}
