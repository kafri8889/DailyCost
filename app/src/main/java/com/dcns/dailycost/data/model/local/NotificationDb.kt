package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Digunakan untuk menyimpan notifikasi ke db
 */
@Parcelize
@Entity(tableName = "notification_table")
data class NotificationDb(
	@PrimaryKey
	@ColumnInfo(name = "id_notification") val id: Int,
	@ColumnInfo(name = "title_notification") val title: String,
	@ColumnInfo(name = "body_notification") val body: String,
	@ColumnInfo(name = "url_notification") val url: String? = null,
	@ColumnInfo(name = "hasBeenRead_notification") val hasBeenRead: Boolean = false
): Parcelable
