package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.note.AddRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.SyncLocalWithRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.UpsertLocalNoteUseCase

data class NoteUseCases(
    val getRemoteNoteUseCase: GetRemoteNoteUseCase,
    val getLocalNoteUseCase: GetLocalNoteUseCase,
    val addRemoteNoteUseCase: AddRemoteNoteUseCase,
    val upsertLocalNoteUseCase: UpsertLocalNoteUseCase,
    val syncLocalWithRemoteNoteUseCase: SyncLocalWithRemoteNoteUseCase
)
