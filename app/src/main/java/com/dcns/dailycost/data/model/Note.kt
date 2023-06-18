package com.dcns.dailycost.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val body: String,
    val date: Long,
    val imageUrl: String,
    val title: String,
    val userId: String,
): Parcelable
