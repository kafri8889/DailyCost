package com.dcns.dailycost.ui.register

import androidx.compose.material3.SnackbarDuration
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.UiEvent

sealed class RegisterUiEvent: UiEvent() {

	class NoInternetConnection(
		override val message: String = asStringResource(R.string.internet_connection_not_available),
		override val actionLabel: String? = null,
		override val withDismissAction: Boolean = true,
		override val duration: SnackbarDuration = SnackbarDuration.Short,
		override val data: Any? = null,
	): ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

}