package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.extension.toNoteDb

class SyncLocalWithRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {

    suspend operator fun invoke(
        remoteNotes: List<Note>
    ) {
        noteRepository.upsertNote(
            *remoteNotes
                .map { it.toNoteDb() }
                .toTypedArray()
        )

        noteRepository.deleteNoteExcept(
            remoteNotes
                .map { it.id }
        )
    }

}