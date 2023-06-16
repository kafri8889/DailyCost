package com.dcns.dailycost.foundation.base

import com.dcns.dailycost.foundation.base.UiEvent

sealed class UiEventResult {
	
	/**
	 * @param [UiEvent] sent, can be used as ID
	 */
	data class ActionPerformed(val event: UiEvent): UiEventResult()
	
	/**
	 * @param [UiEvent] sent, can be used as ID
	 */
	data class Dismissed(val event: UiEvent): UiEventResult()
	
}