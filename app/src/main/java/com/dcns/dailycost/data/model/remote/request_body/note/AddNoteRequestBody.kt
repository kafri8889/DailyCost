package com.dcns.dailycost.data.model.remote.request_body.note

import com.dcns.dailycost.foundation.common.RetrofitRequestBody

data class AddNoteRequestBody(
    val userId: Int,
    val title: String,
    val file: String,
    val body: String,
    val date: String
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "body" to body,
            "file" to file,
            "date" to date,
            "user_id" to userId,
        )
    }
}

