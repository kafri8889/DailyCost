package com.dcns.dailycost.foundation.nav_type

import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.TransactionMode
import timber.log.Timber

val NavType.Companion.TransactionModeNavType: NavType<TransactionMode>
	get() = object: NavType<TransactionMode>(true) {
		override val name: String
			get() = "transaction_mode"

		override fun get(bundle: Bundle, key: String): TransactionMode? {
			val value = bundle.getString(key)
			Timber.i("transaction get: $value")
			return if (value != null) TransactionMode.valueOf(value) else null
		}

		override fun parseValue(value: String): TransactionMode {
			Timber.i("transaction parse: $value")
			return TransactionMode.valueOf(value)
		}

		override fun put(bundle: Bundle, key: String, value: TransactionMode) {
			Timber.i("transaction put: $value")
			bundle.putSerializable(key, value)
		}
	}
