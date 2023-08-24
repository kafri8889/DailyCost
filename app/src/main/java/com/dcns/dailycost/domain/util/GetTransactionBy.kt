package com.dcns.dailycost.domain.util

sealed class GetTransactionBy {

	data class ID(val id: Int): GetTransactionBy()

	object All: GetTransactionBy()

}