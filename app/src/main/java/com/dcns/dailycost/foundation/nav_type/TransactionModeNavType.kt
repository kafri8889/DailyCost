package com.dcns.dailycost.foundation.nav_type

import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.ActionMode
import timber.log.Timber

val NavType.Companion.ActionModeNavType: NavType<ActionMode>
	get() = object: NavType<ActionMode>(true) {
		override val name: String
			get() = "transaction_mode"

		override fun get(bundle: Bundle, key: String): ActionMode? {
			val value = bundle.getString(key)
			Timber.i("transaction get: $value")
			return if (value != null) ActionMode.valueOf(value) else null
		}

		override fun parseValue(value: String): ActionMode {
			Timber.i("transaction parse: $value")
			return ActionMode.valueOf(value)
		}

		override fun put(bundle: Bundle, key: String, value: ActionMode) {
			Timber.i("transaction put: $value")
			bundle.putSerializable(key, value)
		}
	}
