package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class EditNoteResponseData(
    @SerializedName("body") val body: String,
    @SerializedName("catatan_id") val id: Int,
    @SerializedName("title") val title: String
)