package com.dcns.dailycost.foundation.localized

import com.dcns.dailycost.data.Language
import com.dcns.dailycost.foundation.base.UiEvent

class LocalizedUiEvent: UiEvent() {

    data class ApplyLanguage(val language: Language): UiEvent()

    data class LanguageChanged(val language: Language): UiEvent()

}