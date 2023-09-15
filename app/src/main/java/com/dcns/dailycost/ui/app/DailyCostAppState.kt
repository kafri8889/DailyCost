package com.dcns.dailycost.ui.app

import android.os.Parcelable
import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserBalance
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyCostAppState(
	val userBalance: UserBalance = ProtoUserBalance().toUserBalance(),
	val userCredential: UserCredential? = null,
	val currentDestinationRoute: String = "",
	val language: Language = Language.English,
	/**
	 * Digunakan untuk menentukan apakah navController diizinkan untuk menavigasi (lihat dibagian "navigasi otomatis")
	 *
	 * Tujuan variable ini dibuat adalah untuk mencegah navController menavigasi saat user masuk melalui deeplink
	 */
	val canNavigate: Boolean = true,
	/**
	 * if secure app with biometric enabled
	 */
	val isSecureAppEnabled: Boolean = false,
	val isBiometricAuthenticated: Boolean = false,
	val userFirstEnteredApp: Boolean = true,
	val isFirstInstall: Boolean? = null,
): Parcelable
