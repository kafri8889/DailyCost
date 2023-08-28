package com.dcns.dailycost.foundation.nav_type

import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.WalletsScreenMode
import timber.log.Timber

val NavType.Companion.WalletsScreenModeNavType: NavType<WalletsScreenMode>
	get() = object: NavType<WalletsScreenMode>(true) {
		override val name: String
			get() = "wallets_screen_mode_mode"

		override fun get(bundle: Bundle, key: String): WalletsScreenMode? {
			val value = bundle.getString(key)
			Timber.i("wallets screen mode get: $value")
			return if (value != null) WalletsScreenMode.valueOf(value) else null
		}

		override fun parseValue(value: String): WalletsScreenMode {
			Timber.i("wallets screen mode parse: $value")
			return WalletsScreenMode.valueOf(value)
		}

		override fun put(bundle: Bundle, key: String, value: WalletsScreenMode) {
			Timber.i("wallets screen mode put: $value")
			bundle.putSerializable(key, value)
		}
	}
