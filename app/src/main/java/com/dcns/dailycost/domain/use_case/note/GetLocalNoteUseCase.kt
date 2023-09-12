package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.foundation.extension.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Use case untuk mendapatkan catatan dari lokal database
 */
class GetLocalNoteUseCase(
	private val noteRepository: INoteRepository
) {

	operator fun invoke(
		getNoteBy: GetNoteBy = GetNoteBy.All
	): Flow<List<Note>> {
		val flow = when (getNoteBy) {
			is GetNoteBy.UserID -> noteRepository.getLocalNoteByUserId(getNoteBy.userId)
			is GetNoteBy.ID -> noteRepository.getLocalNoteById(getNoteBy.id)
				.filterNotNull() // Filter value yg tidak null
				.map { listOf(it) }

			GetNoteBy.All -> noteRepository.getAllLocalNote()
		}

		return flow
			.map { it.map { it.toNote() } }
	}

}