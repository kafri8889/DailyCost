package com.dcns.dailycost.ui.create_edit_note

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class CreateEditNoteViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val noteUseCases: NoteUseCases
): BaseViewModel<CreateEditNoteState, CreateEditNoteAction>() {

    init {
    }

    override fun defaultState(): CreateEditNoteState = CreateEditNoteState()

    override fun onAction(action: CreateEditNoteAction) {
        when(action) {
            is CreateEditNoteAction.UpdateTitle -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            title = action.title
                        )
                    }
                }
            }
            is CreateEditNoteAction.UpdateDescription -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            description = action.description
                        )
                    }
                }
            }
            is CreateEditNoteAction.UpdateDate -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            date = action.date
                        )
                    }
                }
            }
            is CreateEditNoteAction.UpdateImage -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            uri = action.uri
                        )
                    }
                }
            }
            is CreateEditNoteAction.CreateNote -> {
                viewModelScope.launch {
                    val mState = state.value
//                  Create Post Request
                    val file = File(mState.uri?.path ?: "")
                    val note = mState.date?.let {
                        Note(
                            id = "1",
                            body = mState.description,
                            createdAt = it,
                            imageUrl = "blah",
                            title = mState.title,
                            userId = "3"
                        )
                    }
                    if (note != null) {
                        launch {
                            noteUseCases.addRemoteNoteUseCase(
                                image = file,
                                note = note,
                                token = "Hello token"
                            )
                        }
                    }

                }
            }
            is CreateEditNoteAction.EditNote -> {
                viewModelScope.launch {
//                    todo
                }
            }
        }
    }

}