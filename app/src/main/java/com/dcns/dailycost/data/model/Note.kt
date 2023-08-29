package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.foundation.common.SortableByDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
	val id: String,
	val body: String,
	val createdAt: Long,
	val imageUrl: String,
	val title: String,
	val userId: String,
): Parcelable, SortableByDate {

	override val date: Long
		get() = createdAt

}
