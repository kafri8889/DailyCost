package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class AddNoteResponseData(
    @SerializedName("catatan_id") val noteId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("user_id") val userId: Int
)