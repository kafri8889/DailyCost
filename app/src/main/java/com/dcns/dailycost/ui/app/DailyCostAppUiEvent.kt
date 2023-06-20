package com.dcns.dailycost.ui.app

import com.dcns.dailycost.foundation.base.UiEvent

sealed class DailyCostAppUiEvent: UiEvent {

    object LanguageChanged: UiEvent

}