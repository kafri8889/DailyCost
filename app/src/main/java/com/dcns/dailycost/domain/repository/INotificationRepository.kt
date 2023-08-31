package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.NotificationDb
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {

	fun getAllNotification(): Flow<List<NotificationDb>>

	suspend fun updateNotification(vararg notificationDb: NotificationDb)

	suspend fun insertNotification(vararg notificationDb: NotificationDb)

}