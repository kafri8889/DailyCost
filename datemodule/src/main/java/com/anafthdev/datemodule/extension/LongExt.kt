package com.anafthdev.datemodule.extension

import com.anafthdev.datemodule.DateModule

/**
 * Check if given date is today
 */
fun Long.isToday(): Boolean = DateModule.isToday(this)

/**
 * Check if given date is yesterday
 */
fun Long.isYesterday(): Boolean = DateModule.isYesterday(this)

/**
 * Check if given date is tomorrow
 */
fun Long.isTomorrow(): Boolean = DateModule.isTomorrow(this)

/**
 * @return time in milliseconds for the previous day from current date
 */
fun Long.yesterday(): Long = DateModule.yesterday(this)

/**
 * @return time in milliseconds for the next day from current date
 */
fun Long.tomorrow(): Long = DateModule.tomorrow(this)