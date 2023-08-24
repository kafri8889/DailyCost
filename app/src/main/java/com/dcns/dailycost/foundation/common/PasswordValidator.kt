package com.dcns.dailycost.foundation.common

object PasswordValidator: Validator {

	private const val MIN_LENGTH = 8

	class DigitMissingException: Exception()
	class BelowMinLengthException: Exception()
	class LowerCaseMissingException: Exception()
	class UpperCaseMissingException: Exception()

	private fun containLowerCase(input: String): Boolean {
		return input.any { it.isLowerCase() }
	}

	private fun containUpperCase(input: String): Boolean {
		return input.any { it.isUpperCase() }
	}

	private fun containDigit(input: String): Boolean {
		return input.any { it.isDigit() }
	}

	override fun validate(input: String): Result<Boolean> {
		return when {
			input.length < MIN_LENGTH -> Result.failure(BelowMinLengthException())
			!containLowerCase(input) -> Result.failure(LowerCaseMissingException())
			!containUpperCase(input) -> Result.failure(UpperCaseMissingException())
			!containDigit(input) -> Result.failure(DigitMissingException())
			else -> Result.success(true)
		}
	}
}