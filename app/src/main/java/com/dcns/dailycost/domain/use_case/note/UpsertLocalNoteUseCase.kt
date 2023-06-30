package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.extension.toNoteDb

/**
 * Use case untuk memperbarui atau memasukkan catatan ke database lokal
 */
class UpsertLocalNoteUseCase(
    private val noteRepository: INoteRepository
) {

    suspend operator fun invoke(
        vararg note: Note
    ) {
        val notes = note.map { it.toNoteDb() }.toTypedArray()

        noteRepository.upsertNote(*notes)
    }

}