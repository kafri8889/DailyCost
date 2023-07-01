package com.dcns.dailycost.ui.note

import android.content.Context
import android.net.Uri

sealed interface NoteAction {

    data class UpdateTitle(val title: String): NoteAction
    data class UpdateDescription(val description: String): NoteAction
    data class UpdateDate(val date: Long?): NoteAction
    data class UpdateImage(val uri: Uri?): NoteAction
    data class CreateNote(val context: Context): NoteAction
    data class EditNote(val context: Context): NoteAction
}