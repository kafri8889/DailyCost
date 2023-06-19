package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.remote.NoteResponseData
import java.text.SimpleDateFormat
import java.util.Locale

fun NoteResponseData.toNote(): Note {
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    return Note(
        id = id.toString(),
        body = body,
        createdAt = dateFormatter.parse(createdAt)?.time ?: System.currentTimeMillis(),
        imageUrl = imageUrl,
        title = title,
        userId = userId.toString(),
    )
}
