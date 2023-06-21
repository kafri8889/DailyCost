package com.dcns.dailycost.ui.create_edit_note

import android.net.Uri
import androidx.core.net.toUri

data class CreateEditNoteState(
    val isRefreshing: Boolean = false,
    val title: String = "",
    val description: String = "",
    val date: Long? = 0,
    val uri: Uri? = "".toUri()
)