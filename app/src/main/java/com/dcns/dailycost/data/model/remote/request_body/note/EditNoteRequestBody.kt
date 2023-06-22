package com.dcns.dailycost.data.model.remote.request_body.note

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class EditNoteRequestBody(
    @SerializedName("body") val body: String,
    @SerializedName("title") val title: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("catatan_id") val noteId: String
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "catatan_id" to noteId,
            "user_id" to userId,
            "title" to title,
            "body" to body,
        )
    }
}
