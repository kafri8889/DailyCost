package com.dcns.dailycost.domain.util

sealed class GetNotificationBy {

	/**
	 * Mengambil semua notifikasi
	 */
	data object All: GetNotificationBy()

	/**
	 * Mengambil notifikasi yang sudah dibaca
	 */
	data object Read: GetNotificationBy()

	/**
	 * Mengambil notifikasi yang belum dibaca
	 */
	data object Unread: GetNotificationBy()

}