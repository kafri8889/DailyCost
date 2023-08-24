package com.dcns.dailycost.data.model.remote.response.expense

import com.dcns.dailycost.data.model.remote.AddExpenseResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class AddExpenseResponse(
	val data: AddExpenseResponseData,
	val message: String,
	val status: String
): IResponse