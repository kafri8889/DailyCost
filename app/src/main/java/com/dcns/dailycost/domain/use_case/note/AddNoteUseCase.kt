package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.domain.repository.INoteRepository

class AddNoteUseCase(
    private val noteRepository: INoteRepository
) {

//    suspend operator fun invoke(
//        token: String,
//        body: RequestBody
//    ): Response<NoteResponse> {
//        return noteRepository.addNote(token, body)
//    }

}