package com.dcns.dailycost.data

enum class TransactionMode {
	New,
	Edit;

	fun isNew(): Boolean = this == New
	fun isEdit(): Boolean = this == Edit
}