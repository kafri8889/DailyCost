package com.dcns.dailycost.foundation.base

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

	// Key yang digunakan untuk menyimpan dan mengambil state di savedStateHandle
	private val KEY_STATE = "state"

	private val _uiEvent = Channel<UiEvent?>()
	val uiEvent: Flow<UiEvent?> = _uiEvent.receiveAsFlow()

	private val _uiEventResult = Channel<UiEventResult?>()
	val uiEventResult: Flow<UiEventResult?> = _uiEventResult.receiveAsFlow()

	// Key yang digunakan untuk menyimpan dan mengambil state di savedStateHandle
	val state: StateFlow<STATE> = savedStateHandle.getStateFlow(KEY_STATE, defaultState)

	abstract fun onAction(action: ACTION)

	/**
	 * Function yang digunakan untuk memperbarui state dari [savedStateHandle]
	 *
	 * @param newState parameter ini akan memberikan state saat ini sebagai `this`.
	 */
	protected fun updateState(newState: STATE.() -> STATE) {
		// get current state
		savedStateHandle.get<STATE>(KEY_STATE)?.let { state ->
			// simpan state baru ke savedStateHandle
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