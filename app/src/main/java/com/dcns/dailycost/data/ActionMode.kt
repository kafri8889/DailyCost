package com.dcns.dailycost.data

enum class ActionMode {
	New,
	Edit,
	View;

	fun isNew(): Boolean = this == New
	fun isEdit(): Boolean = this == Edit
	fun isView(): Boolean = this == View
}