package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.data.model.remote.EditNoteResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class EditNoteResponse(
	val data: EditNoteResponseData,
	val message: String,
	val status: String
): IResponse