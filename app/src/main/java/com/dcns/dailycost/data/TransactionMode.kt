package com.dcns.dailycost.data

enum class TransactionMode {
	New,
	@Deprecated("kayaknya ga ada fitur edit transaksi", replaceWith = ReplaceWith("New"))
	Edit;

	fun isNew(): Boolean = this == New
	fun isEdit(): Boolean = this == Edit
}