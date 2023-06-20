package com.dcns.dailycost.foundation.localized.data

import androidx.compose.runtime.compositionLocalOf
import com.dcns.dailycost.data.Language

interface LocalizedController {
	
	fun setLanguage(language: Language)
	
}

class LocalizedWrapper(
	private val onLanguageSet: (Language) -> Unit = {}
): LocalizedController {
	override fun setLanguage(language: Language) { onLanguageSet(language) }
}

val LocalLocalizedController = compositionLocalOf<LocalizedController> { LocalizedWrapper() }
