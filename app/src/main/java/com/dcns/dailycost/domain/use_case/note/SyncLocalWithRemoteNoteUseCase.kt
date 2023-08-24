package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.extension.toNoteDb

/**
 * Use case untuk mengsinkronisasikan catatan dari remote ke lokal
 */
class SyncLocalWithRemoteNoteUseCase(
	private val noteRepository: INoteRepository
) {

	suspend operator fun invoke(
		remoteNotes: List<Note>
	) {
		// Perbarui note lokal dengan note remote
		// Jika note dari remote ada di note lokal (id-nya sama)
		// Update note lokal dengan data dari note remote
		// Jika tidak (id dari remote tidak ada di lokal)
		// Insert note dari remote ke lokal
		noteRepository.upsertNote(
			*remoteNotes
				.map { it.toNoteDb() }
				.toTypedArray()
		)

		// Hapus semua note di database lokal
		// Kecuali note yg id-nya ada di [remoteNotes]
		noteRepository.deleteNoteExcept(
			remoteNotes
				.map { it.id }
		)
	}

}