package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dcns.dailycost.data.model.local.NotificationDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

	@Query("SELECT * FROM notification_table")
	fun getAllNotification(): Flow<List<NotificationDb>>

	@Update
	fun updateNotification(vararg notificationDb: NotificationDb)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertNotification(vararg notificationDb: NotificationDb)

}