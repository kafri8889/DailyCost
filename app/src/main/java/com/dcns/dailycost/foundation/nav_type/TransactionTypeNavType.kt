package com.dcns.dailycost.foundation.nav_type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.TransactionType
import com.google.gson.Gson
import timber.log.Timber

val NavType.Companion.TransactionTypeNavType: NavType<TransactionType>
	get() = object: NavType<TransactionType>(true) {
		override val name: String
			get() = "transaction_type"

		override fun get(bundle: Bundle, key: String): TransactionType? {
			return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(key, TransactionType::class.java)
			else bundle.getParcelable(key)
		}

		override fun parseValue(value: String): TransactionType {
			Timber.i("transaction parse: $value")
			return Gson().fromJson(value, TransactionType::class.java)
		}

		override fun put(bundle: Bundle, key: String, value: TransactionType) {
			Timber.i("transaction put: $value")
			bundle.putParcelable(key, value)
		}
	}
