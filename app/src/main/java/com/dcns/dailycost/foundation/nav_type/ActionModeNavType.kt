package com.dcns.dailycost.foundation.nav_type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.ActionMode
import com.google.gson.Gson
import timber.log.Timber

val NavType.Companion.ActionModeNavType: NavType<ActionMode>
	get() = object: NavType<ActionMode>(true) {
		override val name: String
			get() = "action_mode"

		override fun get(bundle: Bundle, key: String): ActionMode? {
			return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(key, ActionMode::class.java)
			else bundle.getParcelable(key)
		}

		override fun parseValue(value: String): ActionMode {
			Timber.i("action parse: $value")
			return Gson().fromJson(value, ActionMode::class.java)
		}

		override fun put(bundle: Bundle, key: String, value: ActionMode) {
			Timber.i("action put: $value")
			bundle.putParcelable(key, value)
		}
	}
