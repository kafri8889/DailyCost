package com.dcns.dailycost.foundation.base

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *  kelas dasar (base class) untuk view model.
 *  Ini berarti kelas ini memberikan kerangka dasar untuk view model
 *  yang akan diturunkan (derived) oleh kelas-kelas lain.
 *
 *  @param savedStateHandle savedStateHandle yang digunakan untuk menyimpan state
 *  @param defaultState default state
 *
 *  @author kafri8889
 */
abstract class BaseViewModel<STATE: Parcelable, ACTION>(
	private val savedStateHandle: SavedStateHandle,
	private val defaultState: STATE
): ViewModel() {

	private val KEY_STATE = "state"
	private val typeToken = object : TypeToken<STATE>() {}

	private val _uiEvent = Channel<UiEvent?>()
	val uiEvent: Flow<UiEvent?> = _uiEvent.receiveAsFlow()

	private val _uiEventResult = Channel<UiEventResult?>()
	val uiEventResult: Flow<UiEventResult?> = _uiEventResult.receiveAsFlow()

	private val _state = MutableStateFlow(defaultState)
	val state: StateFlow<STATE> = _state

	init {
		viewModelScope.launch {
			savedStateHandle.getStateFlow(KEY_STATE, defaultState)
				.collectLatest { state ->
					_state.update { state }
				}
		}
	}

	abstract fun onAction(action: ACTION)

	protected fun updateState(newState: STATE.() -> STATE) {
		savedStateHandle.get<STATE>(KEY_STATE)?.let { state ->
			savedStateHandle[KEY_STATE] = newState(state)
		}
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