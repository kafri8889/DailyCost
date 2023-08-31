package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.model.local.NotificationDb
import kotlinx.parcelize.Parcelize

/**
 * Digunakan untuk fitur notifikasi
 * @see [NotificationDb]
 */
@Parcelize
data class Notification(
	val id: Int,
	val title: String,
	val body: String,
	val url: String? = null,
	val hasBeenRead: Boolean = false
): Parcelable
