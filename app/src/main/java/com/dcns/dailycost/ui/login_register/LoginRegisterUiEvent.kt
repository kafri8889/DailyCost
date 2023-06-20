package com.dcns.dailycost.ui.login_register

import androidx.compose.material3.SnackbarDuration
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.UiEvent

sealed class LoginRegisterUiEvent: UiEvent {

    class NoInternetConnection(
        override val message: String = UiEvent.asStringResource(R.string.internet_connection_not_available),
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = true,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val data: Any? = null,
    ): UiEvent.ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

}