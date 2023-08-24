package com.dcns.dailycost.foundation.common

object EmailValidator: Validator {

	override fun validate(input: String): Result<Boolean> {
		val contain = input.contains("^[a-zA-Z0-9._%+-]+@gmail\\.com\$".toRegex())

		return if (contain) Result.success(true)
		else Result.failure(IllegalArgumentException("email not valid"))
	}
}