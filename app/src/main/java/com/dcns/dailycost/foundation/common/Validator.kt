package com.dcns.dailycost.foundation.common

interface Validator {

	fun validate(input: String): Result<Boolean>

}