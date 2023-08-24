package com.dcns.dailycost.foundation.extension

import com.google.gson.Gson

fun <T> String.fromJson(klass: Class<T>): T {
	return Gson().fromJson(this, klass)
}

fun String.uppercaseFirstLetterInWord(
	splitDelimiters: String = " ",
	separator: String = " ",
): String {
	return split(splitDelimiters).joinToString(separator) {
		it.getOrNull(0)?.uppercase() + it.slice(
			1 until it.length
		)
	}
}
