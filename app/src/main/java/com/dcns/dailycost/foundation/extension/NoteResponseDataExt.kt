package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.remote.NoteResponseData

fun NoteResponseData.toNote(): Note {
    return Note(
        id = id.toString(),
        body = body,
        createdAt = createdAtEpoch,
        imageUrl = imageUrl,
        title = title,
        userId = userId.toString(),
    )
}
