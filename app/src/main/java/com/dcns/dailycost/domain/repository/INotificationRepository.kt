package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.NotificationDb
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {

	/**
	 * Mengambil semua notifikasi
	 */
	fun getAllNotification(): Flow<List<NotificationDb>>

	/**
	 * Mengambil notifikasi yang belum dibaca
	 */
	fun getUnreadNotification(): Flow<List<NotificationDb>>

	/**
	 * Mengambil notifikasi yang sudah dibaca
	 */

	fun getReadNotification(): Flow<List<NotificationDb>>

	suspend fun updateNotification(vararg notificationDb: NotificationDb)

	suspend fun insertNotification(vararg notificationDb: NotificationDb)

}