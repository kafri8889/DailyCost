package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.model.local.NotificationDb
import kotlinx.parcelize.Parcelize

/**
 * Data class yang mewakili sebuah notifikasi.
 *
 * @property id id di database.
 * @property title Judul dari notifikasi.
 * @property body Isi atau konten dari notifikasi.
 * @property url URL opsional yang terkait dengan notifikasi (default null).
 * @property hasBeenRead boolean yang menunjukkan apakah notifikasi sudah dibaca (default false).
 * @property sentTimeMillis Timestamp (dalam milidetik) yang mewakili kapan notifikasi dikirimkan.
 *
 * @see [NotificationDb]
 */
@Parcelize
data class Notification(
	val id: Int,
	val title: String,
	val body: String,
	val url: String? = null,
	val hasBeenRead: Boolean = false,
	val sentTimeMillis: Long = System.currentTimeMillis()
): Parcelable
