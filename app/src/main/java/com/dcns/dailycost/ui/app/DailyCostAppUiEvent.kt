package com.dcns.dailycost.ui.app

import android.net.Uri
import android.widget.Toast
import com.dcns.dailycost.R
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.foundation.base.UiEvent

sealed class DailyCostAppUiEvent: UiEvent() {

	data class NavigateTo(val dest: TopLevelDestination): UiEvent()

	data class HandleDeepLink(val uri: Uri): UiEvent()

	object LanguageChanged: UiEvent()

	object TokenExpired: ShowToast(
		message = asStringResource(R.string.token_expired),
		length = Toast.LENGTH_LONG
	)

}