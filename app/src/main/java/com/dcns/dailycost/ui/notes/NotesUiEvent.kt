package com.dcns.dailycost.ui.notes

import androidx.compose.material3.SnackbarDuration
import com.dcns.dailycost.foundation.base.UiEvent

sealed class NotesUiEvent: UiEvent() {

	class GetRemoteNoteFailed(
		override val message: String = "",
		override val actionLabel: String? = null,
		override val withDismissAction: Boolean = true,
		override val duration: SnackbarDuration = SnackbarDuration.Short,
		override val data: Any? = null,
	): ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

}