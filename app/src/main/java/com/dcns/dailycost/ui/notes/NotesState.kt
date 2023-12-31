package com.dcns.dailycost.ui.notes

import android.os.Parcelable
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserCredential
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotesState(
	val isRefreshing: Boolean = false,
	val credential: UserCredential = ProtoUserCredential().toUserCredential(),
	val notes: List<Note> = emptyList()
): Parcelable
