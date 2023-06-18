package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note_table")
data class NoteDb(
    @PrimaryKey
    @ColumnInfo(name = "id_note") val id: String,
    @ColumnInfo(name = "body_note") val body: String,
    @ColumnInfo(name = "date_note") val date: Long,
    @ColumnInfo(name = "imageUrl_note") val imageUrl: String,
    @ColumnInfo(name = "title_note") val title: String,
    @ColumnInfo(name = "userId_note") val userId: String,
): Parcelable
