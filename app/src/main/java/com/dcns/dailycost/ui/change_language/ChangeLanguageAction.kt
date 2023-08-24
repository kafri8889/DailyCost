package com.dcns.dailycost.ui.change_language

import com.dcns.dailycost.data.Language

sealed interface ChangeLanguageAction {

	data class ChangeLanguage(val language: Language): ChangeLanguageAction

}