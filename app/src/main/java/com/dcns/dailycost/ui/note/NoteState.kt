package com.dcns.dailycost.ui.note

import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteState(
	val isRefreshing: Boolean = false,
	val title: String = "",
	val description: String = "",
	val date: Long? = 0,
	val uri: Uri? = "".toUri()
): Parcelable