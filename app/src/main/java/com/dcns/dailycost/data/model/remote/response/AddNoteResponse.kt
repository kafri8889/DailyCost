package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.AddNoteResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class AddNoteResponse(
	val data: AddNoteResponseData,
	val message: String,
	val status: String
): IResponse