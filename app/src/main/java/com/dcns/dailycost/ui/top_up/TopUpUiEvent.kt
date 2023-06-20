package com.dcns.dailycost.ui.top_up

import com.dcns.dailycost.foundation.base.UiEvent

sealed class TopUpUiEvent: UiEvent {

    object TopUpSuccess: UiEvent

    data class TopUpFailed(
        val message: String
    ): UiEvent

}