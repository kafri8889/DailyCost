package com.dcns.dailycost.ui.app

import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.foundation.base.UiEvent

sealed class DailyCostAppUiEvent: UiEvent() {

    data class NavigateTo(val dest: TopLevelDestination): UiEvent()

    object LanguageChanged: UiEvent()

}