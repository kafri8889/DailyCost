package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.foundation.common.IResponse
import retrofit2.Response

class GetRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {

    suspend operator fun invoke(
        token: String,
        getNoteBy: GetNoteBy = GetNoteBy.All
    ): Response<out IResponse> {
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