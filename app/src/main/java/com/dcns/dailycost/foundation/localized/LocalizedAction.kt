package com.dcns.dailycost.foundation.localized

import com.dcns.dailycost.data.Language

sealed interface LocalizedAction {
	data class SetLanguage(val lang: Language): LocalizedAction
}