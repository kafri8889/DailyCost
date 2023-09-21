package com.dcns.dailycost.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ActionMode: Parcelable {
	New,
	Edit,
	View;

	fun isNew(): Boolean = this == New
	fun isEdit(): Boolean = this == Edit
	fun isView(): Boolean = this == View
}