package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.NoteResponseData
import com.dcns.dailycost.foundation.common.IResponse

data class NoteResponse(
    val data: List<NoteResponseData>,
    val message: String,
    val status: String
): IResponse