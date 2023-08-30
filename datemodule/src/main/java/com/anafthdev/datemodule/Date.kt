package com.anafthdev.datemodule

import com.anafthdev.datemodule.extension.toCalendarMonth
import com.anafthdev.datemodule.model.Week
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale
import java.util.stream.Collectors
import java.util.stream.IntStream

object DateModule {

	/**
	 * @return time in milliseconds for the previous day from current date [System.currentTimeMillis]
	 */
	fun yesterday(): Long {
		return yesterday(System.currentTimeMillis())
	}

	/**
	 * @return time in milliseconds for the next day from current date [System.currentTimeMillis]
	 */
	fun tomorrow(): Long {
		return tomorrow(System.currentTimeMillis())
	}

	/**
	 * Check if given date is today
	 */
	fun isToday(timeInMillis: Long): Boolean = inSameDay(timeInMillis, System.currentTimeMillis())

	/**
	 * Check if given date is yesterday
	 */
	fun isYesterday(timeInMillis: Long): Boolean = inSameDay(timeInMillis, Instant.now().minusMillis(1 * 86_400_000).toEpochMilli())

	/**
	 * Check if given date is tomorrow
	 */
	fun isTomorrow(timeInMillis: Long): Boolean = inSameDay(timeInMillis, Instant.now().plusMillis(1 * 86_400_000).toEpochMilli())

	/**
	 * @return time in milliseconds for the previous day from given date [timeInMillis]
	 */
	fun yesterday(timeInMillis: Long): Long {
		return Instant.ofEpochMilli(timeInMillis).minusMillis(1 * 86_400_000).toEpochMilli()
	}

	/**
	 * @return time in milliseconds for the next day from given date [timeInMillis]
	 */
	fun tomorrow(timeInMillis: Long): Long {
		return Instant.ofEpochMilli(timeInMillis).plusMillis(1 * 86_400_000).toEpochMilli()
	}

	/**
	 * Check if two date in the same day
	 *
	 * @param t1 First date (time in milliseconds)
	 * @param t2 Second date (time in milliseconds)
	 */
	fun inSameDay(t1: Long, t2: Long): Boolean {
		var same: Boolean
		Calendar.getInstance().apply {
			timeInMillis = t1
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)

			same = get(Calendar.DAY_OF_YEAR) == let {
				it.timeInMillis = t2
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 0)

				it.get(Calendar.DAY_OF_YEAR)
			}
		}

		return same
	}

	/**
	 * Check if two date in the same month
	 *
	 * @param t1 First date (time in milliseconds)
	 * @param t2 Second date (time in milliseconds)
	 */
	fun inSameMonth(t1: Long, t2: Long): Boolean {
		var same: Boolean
		Calendar.getInstance().apply {
			timeInMillis = t1
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)

			same = get(Calendar.MONTH) == let {
				it.timeInMillis = t2
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 0)

				it.get(Calendar.MONTH)
			}
		}

		return same
	}

	/**
	 * Check if two date in the same year
	 *
	 * @param t1 First date (time in milliseconds)
	 * @param t2 Second date (time in milliseconds)
	 */
	fun inSameYear(t1: Long, t2: Long): Boolean {
		var same: Boolean
		Calendar.getInstance().apply {
			timeInMillis = t1
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)

			same = get(Calendar.YEAR) == let {
				it.timeInMillis = t2
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 0)

				it.get(Calendar.YEAR)
			}
		}

		return same
	}
	
	/**
	 * Convert [Calendar.MONTH] to [java.time.Month]
	 *
	 * @return [Month] or null if [month] out of range (0 - 11)
	 */
	fun calendarMonthToJavaTimeMonth(month: Int): Month? {
		return when(month) {
			Calendar.JANUARY -> Month.JANUARY
			Calendar.FEBRUARY -> Month.FEBRUARY
			Calendar.MARCH -> Month.MARCH
			Calendar.APRIL -> Month.APRIL
			Calendar.MAY -> Month.MAY
			Calendar.JUNE -> Month.JUNE
			Calendar.JULY -> Month.JULY
			Calendar.AUGUST -> Month.AUGUST
			Calendar.SEPTEMBER -> Month.SEPTEMBER
			Calendar.OCTOBER -> Month.OCTOBER
			Calendar.NOVEMBER -> Month.NOVEMBER
			Calendar.DECEMBER -> Month.DECEMBER
			else -> null
		}
	}
	
	/**
	 * Convert [java.time.Month] to [Calendar.MONTH]
	 *
	 * for extension [java.time.Month.toCalendarMonth]
	 *
	 * @return [Calendar.MONTH]
	 */
	fun javaTimeMonthToCalendarMonth(month: Month): Int {
		return when(month) {
			Month.JANUARY -> Calendar.JANUARY
			Month.FEBRUARY -> Calendar.FEBRUARY
			Month.MARCH -> Calendar.MARCH
			Month.APRIL -> Calendar.APRIL
			Month.MAY -> Calendar.MAY
			Month.JUNE -> Calendar.JUNE
			Month.JULY -> Calendar.JULY
			Month.AUGUST -> Calendar.AUGUST
			Month.SEPTEMBER -> Calendar.SEPTEMBER
			Month.OCTOBER -> Calendar.OCTOBER
			Month.NOVEMBER -> Calendar.NOVEMBER
			Month.DECEMBER -> Calendar.DECEMBER
		}
	}
	
	/**
	 * Get next month
	 *
	 * if current month is [Month.DECEMBER], next month will be [Month.JANUARY]
	 */
	fun nextMonthOf(month: Month): Month {
		return when {
			month.value + 1 > Month.DECEMBER.value -> Month.JANUARY
			else -> Month.of(month.value + 1)
		}
	}
	
	/**
	 * Get previous month
	 *
	 * if current month is [Month.JANUARY], previous month will be [Month.DECEMBER]
	 */
	fun previousMonthOf(month: Month): Month {
		return when {
			month.value - 1 < Month.JANUARY.value -> Month.DECEMBER
			else -> Month.of(month.value - 1)
		}
	}
	
	/**
	 * Get next month
	 *
	 * if current month is [Month.DECEMBER], next month will be `null`
	 */
	fun nextMonthOrNull(month: Month): Month? {
		return when {
			month.value + 1 > Month.DECEMBER.value -> null
			else -> Month.of(month.value + 1)
		}
	}
	
	/**
	 * Get previous month
	 *
	 * if current month is [Month.JANUARY], previous month will be `null`
	 */
	fun previousMonthOrNull(month: Month): Month? {
		return when {
			month.value - 1 < Month.JANUARY.value -> null
			else -> Month.of(month.value - 1)
		}
	}
	
	/**
	 * Check if current [millis] is first day of the week
	 */
	fun isFirstDayOfWeek(millis: Long): Boolean {
		val cal = Calendar.getInstance().apply {
			timeInMillis = millis
		}
		
		return cal.get(Calendar.DAY_OF_WEEK) == cal.firstDayOfWeek
	}

	/**
	 * Get week from week collection with timeInMillis
	 */
	fun getWeekInWeeks(weeks: Collection<Week>, timeInMillis: Long): Week? {
		weeks.forEach { week ->
			if (timeInMillis in week.firstDay..week.lastDay) return week
		}
		
		return null
	}

	/**
	 * Get available week from [start] to [end]
	 *
	 * @param start (time in milliseconds)
	 * @param end (time in milliseconds)
	 */
	fun getAvailableWeeks(start: Long, end: Long): List<Week> {
		val weeks = arrayListOf<Week>()
		
		val mNew = if (isFirstDayOfWeek(end)) {
			Calendar.getInstance().apply {
				timeInMillis = end
				
				add(Calendar.DAY_OF_MONTH, 1)
			}.timeInMillis
		} else end
		
		val cal = Calendar.getInstance().apply {
			timeInMillis = start
			
			set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 1)
		}
		
		while (cal.timeInMillis < mNew) {
			val first = cal.timeInMillis
			val firstDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

			cal.apply {
				add(Calendar.DAY_OF_WEEK, 6)
				set(Calendar.HOUR_OF_DAY, 23)
				set(Calendar.MINUTE, 59)
				set(Calendar.SECOND, 59)
			}

			val last = cal.timeInMillis
			val lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

			weeks.add(
				Week(
					year = cal.get(Calendar.YEAR),
					month = cal.get(Calendar.MONTH),
					firstDay = first,
					lastDay = last,
					firstDayOfMonth = firstDayOfMonth,
					lastDayOfMonth = lastDayOfMonth
				)
			)
			
			cal.apply {
				add(Calendar.DAY_OF_WEEK, 1)
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 1)
			}
		}
		
		return weeks
	}
	
	/**
	 * Get current week from given [millis]
	 */
	fun getWeek(millis: Long): Week {
		val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
		val monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		
		val days = IntStream.range(0, 7).mapToObj { daysToAdd: Int ->
			monday.plusDays(daysToAdd.toLong())
		}.collect(Collectors.toList())
		
		val first = days.first()
			.atTime(0, 0, 0)
			.atZone(ZoneId.systemDefault())
		
		val last = days.last()
			.atTime(23, 59, 59)
			.atZone(ZoneId.systemDefault())

		val firstInMillis = first.toInstant().toEpochMilli()
		val lastInMillis = last.toInstant().toEpochMilli()
		val firstDayOfMonth = first.dayOfMonth
		val lastDayOfMonth = last.dayOfMonth

		return Week(
			year = date.year,
			month = date.monthValue - 1,  // -1 because convert java month to calendar month
			firstDay = firstInMillis,
			lastDay = lastInMillis,
			firstDayOfMonth = firstDayOfMonth,
			lastDayOfMonth = lastDayOfMonth
		)
	}
	
	fun datesOfWeekDate(date: LocalDate): List<LocalDate> {
		val firstDayOfWeek = date.with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).firstDayOfWeek))
		
		return IntStream.range(0, 7).mapToObj { daysToAdd: Int ->
			firstDayOfWeek.plusDays(daysToAdd.toLong())
		}.collect(Collectors.toList())
	}
}
