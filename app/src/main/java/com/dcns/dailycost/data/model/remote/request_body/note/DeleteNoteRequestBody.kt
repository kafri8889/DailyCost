package com.dcns.dailycost.data.model.remote.request_body.note

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class DeleteNoteRequestBody(
    @SerializedName("user_id") val userId: String,
    @SerializedName("catatan_id") val noteId: String
): RetrofitRequestBody() {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "catatan_id" to noteId,
                "user_id" to userId
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

}
