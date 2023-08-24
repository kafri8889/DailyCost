package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.foundation.base.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Digunakan untuk mengirim/mendapatkan ui event antar view model
 *
 * @author kafri8889
 */
class SharedUiEvent @Inject constructor() {

	private val _uiEvent = MutableStateFlow<UiEvent?>(null)
	val uiEvent: StateFlow<UiEvent?> = _uiEvent

	fun sendUiEvent(event: UiEvent?) {
		_uiEvent.update { event }
	}

}