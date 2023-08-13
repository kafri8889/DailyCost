package com.dcns.dailycost.foundation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *  kelas dasar (base class) untuk view model.
 *  Ini berarti kelas ini memberikan kerangka dasar untuk view model
 *  yang akan diturunkan (derived) oleh kelas-kelas lain.
 *
 *  @author kafri8889
 */
abstract class BaseViewModel<STATE, ACTION>: ViewModel() {
	
	private val _state = MutableStateFlow(this.defaultState())
	val state: StateFlow<STATE> = _state
	
	private val _uiEvent = Channel<UiEvent?>()
	val uiEvent: Flow<UiEvent?> = _uiEvent.receiveAsFlow()
	
	private val _uiEventResult = Channel<UiEventResult?>()
	val uiEventResult: Flow<UiEventResult?> = _uiEventResult.receiveAsFlow()
	
	protected abstract fun defaultState(): STATE
	
	abstract fun onAction(action: ACTION)
	
	protected fun updateState(newState: STATE.() -> STATE) {
		_state.update(newState)
	}
	
	fun resetEvent() {
		viewModelScope.launch {
			_uiEvent.send(null)
		}
	}
	
	fun resetEventResult() {
		viewModelScope.launch {
			_uiEventResult.send(null)
		}
	}
	
	fun sendEvent(uiEvent: UiEvent) {
		viewModelScope.launch {
			_uiEvent.send(uiEvent)
		}
	}
	
	fun sendEventResult(result: UiEventResult) {
		viewModelScope.launch {
			_uiEventResult.send(result)
		}
	}

	fun dismissCurrentSnackbar() {
		viewModelScope.launch {
			sendEvent(UiEvent.DismissCurrentSnackbar)
		}
	}
	
}