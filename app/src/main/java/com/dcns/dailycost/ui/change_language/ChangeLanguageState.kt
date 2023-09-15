package com.dcns.dailycost.ui.change_language

import android.os.Parcelable
import com.dcns.dailycost.data.Language
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeLanguageState(
	val selectedLanguage: Language = Language.English
): Parcelable
