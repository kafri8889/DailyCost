package com.dcns.dailycost.data.model.remote.request_body.expense

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class DeleteExpenseRequestBody(
    @SerializedName("pengeluaran_id") val expenseId: Int,
    @SerializedName("user_id") val userId: Int
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "pengeluaran_id" to expenseId,
            "user_id" to userId
        )
    }

}