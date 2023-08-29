package com.dcns.dailycost.foundation.nav_type

import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.CategoriesScreenMode
import timber.log.Timber

val NavType.Companion.CategoriesScreenModeNavType: NavType<CategoriesScreenMode>
	get() = object: NavType<CategoriesScreenMode>(true) {
		override val name: String
			get() = "categories_screen_mode_mode"

		override fun get(bundle: Bundle, key: String): CategoriesScreenMode? {
			val value = bundle.getString(key)
			Timber.i("categories screen mode get: $value")
			return if (value != null) CategoriesScreenMode.valueOf(value) else null
		}

		override fun parseValue(value: String): CategoriesScreenMode {
			Timber.i("categories screen mode parse: $value")
			return CategoriesScreenMode.valueOf(value)
		}

		override fun put(bundle: Bundle, key: String, value: CategoriesScreenMode) {
			Timber.i("categories screen mode put: $value")
			bundle.putSerializable(key, value)
		}
	}
