package com.dcns.dailycost.ui.change_language

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserPreferenceRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.base.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLanguageViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
): BaseViewModel<ChangeLanguageState, ChangeLanguageAction, UiEvent>() {

    init {
        viewModelScope.launch {
            userPreferenceRepository.getUserPreference.collect { pref ->
                updateState {
                    copy(
                        selectedLanguage = pref.language
                    )
                }
            }
        }
    }

    override fun defaultState(): ChangeLanguageState = ChangeLanguageState()

    override fun onAction(action: ChangeLanguageAction) {
        when (action) {
            is ChangeLanguageAction.ChangeLanguage -> {
                viewModelScope.launch {
                    userPreferenceRepository.setLanguage(action.language)
                }
            }
        }
    }
}