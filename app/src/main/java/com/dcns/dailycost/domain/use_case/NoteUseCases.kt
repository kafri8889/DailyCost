package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.note.AddNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetRemoteNoteUseCase

data class NoteUseCases(
    val getRemoteNoteUseCase: GetRemoteNoteUseCase,
    val getLocalNoteUseCase: GetLocalNoteUseCase,
    val addNoteUseCase: AddNoteUseCase
)
