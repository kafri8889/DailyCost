package com.dcns.dailycost.ui.note

import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): BaseViewModel<NoteState, NoteAction, NoteUiEvent>() {

    override fun defaultState(): NoteState = NoteState()

    override fun onAction(action: NoteAction) {

    }
}