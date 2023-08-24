package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class NoteResponseData(
	@SerializedName("body") val body: String,
	@SerializedName("created_at") val createdAt: String,
	@SerializedName("created_at_epoch") val createdAtEpoch: Long,
	@SerializedName("catatan_id") val id: Int,
	@SerializedName("url") val imageUrl: String,
	@SerializedName("title") val title: String,
	@SerializedName("user_id") val userId: Int
)