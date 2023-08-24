package com.dcns.dailycost.foundation.nav_type

import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.TransactionType
import timber.log.Timber

val NavType.Companion.TransactionTypeNavType: NavType<TransactionType>
	get() = object: NavType<TransactionType>(true) {
		override val name: String
			get() = "transaction_type"

		override fun get(bundle: Bundle, key: String): TransactionType? {
			val value = bundle.getString(key)
			Timber.i("transaction get: $value")
			return if (value != null) TransactionType.valueOf(value) else null
		}

		override fun parseValue(value: String): TransactionType {
			Timber.i("transaction parse: $value")
			return TransactionType.valueOf(value)
		}

		override fun put(bundle: Bundle, key: String, value: TransactionType) {
			Timber.i("transaction put: $value")
			bundle.putSerializable(key, value)
		}
	}
