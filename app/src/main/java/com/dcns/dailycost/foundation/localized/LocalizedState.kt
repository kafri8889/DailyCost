package com.dcns.dailycost.foundation.localized

import android.os.Parcelable
import com.dcns.dailycost.data.Language
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalizedState(
	val language: Language = Language.English
): Parcelable
