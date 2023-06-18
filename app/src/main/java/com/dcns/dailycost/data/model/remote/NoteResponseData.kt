package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class NoteResponseData(
    @SerializedName("body") val body: String,
    @SerializedName("date") val date: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("user_id") val userId: Int
)