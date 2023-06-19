package com.dcns.dailycost.ui.note

import com.dcns.dailycost.data.model.Note

data class NoteState(
    val notes: List<Note> = emptyList()
)
