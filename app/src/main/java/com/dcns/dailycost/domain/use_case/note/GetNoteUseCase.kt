package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.domain.util.GetNoteMethod
import com.dcns.dailycost.foundation.common.IResponse
import retrofit2.Response

class GetNoteUseCase(
    private val noteRepository: INoteRepository
) {

    suspend operator fun invoke(
        method: GetNoteMethod,
        getNoteBy: GetNoteBy = GetNoteBy.All
    ): Response<out IResponse> {
        return when (method) {
            is GetNoteMethod.Api -> {
                when (getNoteBy) {
                    is GetNoteBy.ID -> noteRepository.getNoteById(
                        token = method.token,
                        userId = getNoteBy.userId
                    )
                    GetNoteBy.All -> noteRepository.getNote(
                        token = method.token
                    )
                }
            }
            is GetNoteMethod.Local -> {
                // TODO: Get data dari database
                noteRepository.getNote("")
            }
        }
    }

}