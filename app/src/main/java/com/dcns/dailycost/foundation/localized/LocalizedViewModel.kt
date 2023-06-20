package com.dcns.dailycost.foundation.localized

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserPreferenceRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalizedViewModel @Inject constructor(
	private val userPreferenceRepository: UserPreferenceRepository
): BaseViewModel<LocalizedState, LocalizedAction, LocalizedUiEvent>() {
	
	init {
		viewModelScope.launch {
			userPreferenceRepository.getUserPreference.collect { pref ->
				sendEvent(LocalizedUiEvent.LanguageChanged(pref.language))
				sendEvent(LocalizedUiEvent.ApplyLanguage(pref.language))
				updateState {
					copy(
						language = pref.language
					)
				}
			}
		}
	}

	override fun defaultState(): LocalizedState = LocalizedState()

	override fun onAction(action: LocalizedAction) {
		when (action) {
			is LocalizedAction.SetLanguage -> {
				viewModelScope.launch {
					userPreferenceRepository.setLanguage(action.lang)
				}
			}
		}
	}
}