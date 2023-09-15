package com.dcns.dailycost.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val userCredentialUseCases: UserCredentialUseCases,
	private val noteUseCases: NoteUseCases
): BaseViewModel<NoteState, NoteAction>(savedStateHandle, NoteState()) {

	override fun onAction(action: NoteAction) {
		when (action) {
			is NoteAction.UpdateTitle -> {
				viewModelScope.launch {
					updateState {
						copy(
							title = action.title
						)
					}
				}
			}

			is NoteAction.UpdateDescription -> {
				viewModelScope.launch {
					updateState {
						copy(
							description = action.description
						)
					}
				}
			}

			is NoteAction.UpdateDate -> {
				viewModelScope.launch {
					updateState {
						copy(
							date = action.date
						)
					}
				}
			}

			is NoteAction.UpdateImage -> {
				viewModelScope.launch {
					updateState {
						copy(
							uri = action.uri
						)
					}
				}
			}

			is NoteAction.CreateNote -> {
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
//                            noteUseCases.addRemoteNoteUseCase(
//                                image = file,
//                                note = note,
//                                token = "Hello token",
//                                userId = mState
//                            )
						}
					}

				}
			}

			is NoteAction.EditNote -> {
				viewModelScope.launch {
//                    todo
				}
			}
		}
	}

}