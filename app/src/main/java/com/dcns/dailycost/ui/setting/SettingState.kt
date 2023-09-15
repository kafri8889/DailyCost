package com.dcns.dailycost.ui.setting

import android.os.Parcelable
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserCredential
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingState(
	val userCredential: UserCredential = ProtoUserCredential().toUserCredential(),
	val language: Language = Language.English,
	/**
	 * if secure app with biometric enabled
	 */
	val isSecureAppEnabled: Boolean = false,
	val defaultBalanceVisibility: Boolean = true,
): Parcelable
