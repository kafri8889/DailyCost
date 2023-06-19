package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import retrofit2.Response

class GetRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {

    suspend operator fun invoke(
        token: String,
        getNoteBy: GetNoteBy = GetNoteBy.All
    ): Response<NoteResponse> {
        return when (getNoteBy) {
            is GetNoteBy.UserID -> noteRepository.getNoteByIdRemote(
                token = token,
                userId = getNoteBy.userId
            )
            is GetNoteBy.ID -> noteRepository.getNoteByIdRemote(
                token = token,
                userId = getNoteBy.id
            )
            GetNoteBy.All -> noteRepository.getNoteRemote(
                token = token
            )
        }
    }

}