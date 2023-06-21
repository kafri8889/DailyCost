package com.dcns.dailycost.ui.create_edit_note

import android.content.Context
import android.net.Uri

sealed interface CreateEditNoteAction {

    data class UpdateTitle(val title: String): CreateEditNoteAction
    data class UpdateDescription(val description: String): CreateEditNoteAction
    data class UpdateDate(val date: Long?): CreateEditNoteAction
    data class UpdateImage(val uri: Uri?): CreateEditNoteAction
    data class CreateNote(val context: Context): CreateEditNoteAction
    data class EditNote(val context: Context): CreateEditNoteAction
}