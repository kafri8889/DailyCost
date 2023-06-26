package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import retrofit2.Response

class GetRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {

    /**
     * @param getNoteBy Get remote note only support [GetNoteBy.UserID]
     */
    suspend operator fun invoke(
        token: String,
        getNoteBy: GetNoteBy
    ): Response<GetNoteResponse> {
        return when (getNoteBy) {
            is GetNoteBy.UserID -> noteRepository.getNoteByIdRemote(
                token = token,
                userId = getNoteBy.userId
            )
            else -> throw IllegalStateException("${getNoteBy.javaClass.name} not supported")
        }
    }

}