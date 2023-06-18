package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.local.NoteDb

fun NoteDb.toNote(): Note {
    return Note(id, body, date, imageUrl, title, userId)
}

fun Note.toNoteDb(): NoteDb {
    return NoteDb(id, body, date, imageUrl, title, userId)
}