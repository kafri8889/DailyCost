package com.dcns.dailycost.ui.create_note

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.extension.toNote
import com.dcns.dailycost.ui.note.NoteAction
import com.dcns.dailycost.ui.note.NoteState
import com.dcns.dailycost.ui.note.NoteUiEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CreateEditNoteViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val noteUseCases: NoteUseCases
): BaseViewModel<NoteState, NoteAction, NoteUiEvent>() {

    init {
        viewModelScope.launch {
            userCredentialRepository.getUserCredential.collect { cred ->
                updateState {
                    copy(
                        credential = cred
                    )
                }
            }
        }

        viewModelScope.launch {
            noteUseCases.getLocalNoteUseCase().collect { notes ->
                updateState {
                    copy(
                        notes = notes
                    )
                }
            }
        }
    }

    override fun defaultState(): NoteState = NoteState()

    override fun onAction(action: NoteAction) {
        when (action) {
            NoteAction.Refresh -> {
                viewModelScope.launch {
                    val mState = state.value

                    updateState {
                        copy(
                            isRefreshing = true
                        )
                    }

                    noteUseCases.getRemoteNoteUseCase(
                        token = mState.credential.getAuthToken(),
                        getNoteBy = GetNoteBy.UserID(mState.credential.id.toInt())
                    ).let { response ->
                        // 401: data not found
                        if (response.isSuccessful) {
                            val noteResponse = response.body()

                            noteResponse?.let {
                                Timber.i("upserting notes to db...")
                                launch(Dispatchers.IO) {
                                    noteUseCases.upsertLocalNoteUseCase(
                                        *noteResponse.data
                                            .map { it.toNote() }
                                            .toTypedArray()
                                    )
                                }
                            }

                            updateState {
                                copy(
                                    isRefreshing = false
                                )
                            }

                            return@launch
                        }

                        val errorResponse = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )

                        sendEvent(NoteUiEvent.GetRemoteNoteFailed(errorResponse.message))

                        updateState {
                            copy(
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }
}