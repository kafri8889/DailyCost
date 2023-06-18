package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.foundation.extension.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetLocalNoteUseCase(
    private val noteRepository: INoteRepository
) {

    operator fun invoke(
        getNoteBy: GetNoteBy = GetNoteBy.All
    ): Flow<List<Note>> {
        val flow = when (getNoteBy) {
            is GetNoteBy.UserID -> noteRepository.getNoteByUserIdLocal(getNoteBy.userId)
            is GetNoteBy.ID -> noteRepository.getNoteByIdLocal(getNoteBy.id)
                .filterNotNull()
                .map { listOf(it) }
            GetNoteBy.All -> noteRepository.getAllNoteLocal()
        }

        return flow
            .map { it.map { it.toNote() } }
    }

}