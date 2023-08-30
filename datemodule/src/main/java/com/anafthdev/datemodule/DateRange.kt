package com.anafthdev.datemodule

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class DateRange(val start: Long, val end: Long): Parcelable {

	override fun toString(): String {
		return Gson().toJson(this)
	}

	companion object {

		/**
		 * @return [DateRange] from 00:00:01 to 23:59:59
		 */
		fun day(millis: Long): DateRange {
			val cal = Calendar.getInstance().apply {
				timeInMillis = millis
				
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 1)
			}
			
			val start = cal.timeInMillis
			
			cal.apply {
				set(Calendar.HOUR_OF_DAY, 23)
				set(Calendar.MINUTE, 59)
				set(Calendar.SECOND, 59)
			}
			
			val end = cal.timeInMillis
			
			return DateRange(start, end)
		}

		/**
		 * @return [DateRange] from 01-month-year 00:00:01 to max_day_of_month-month-year 23:59:59 (first day until last day in month)
		 */
		fun month(millis: Long): DateRange {
			val cal = Calendar.getInstance().apply {
				timeInMillis = millis
				
				set(Calendar.DAY_OF_MONTH, 1)
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 1)
			}
			
			val start = cal.timeInMillis
			
			cal.apply {
				set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
				set(Calendar.HOUR_OF_DAY, 23)
				set(Calendar.MINUTE, 59)
				set(Calendar.SECOND, 59)
			}
			
			val end = cal.timeInMillis
			
			return DateRange(start, end)
		}

		/**
		 * @return [DateRange] from 01-01-year 00:00:01 to max_day_of_month-12-year 23:59:59 (first day until last day in year)
		 */
		fun year(millis: Long): DateRange {
			val cal = Calendar.getInstance().apply {
				timeInMillis = millis
				
				set(Calendar.DAY_OF_YEAR, 1)
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 1)
			}
			
			val start = cal.timeInMillis
			
			cal.apply {
				set(Calendar.DAY_OF_YEAR, getActualMaximum(Calendar.DAY_OF_YEAR))
				set(Calendar.HOUR_OF_DAY, 23)
				set(Calendar.MINUTE, 59)
				set(Calendar.SECOND, 59)
			}
			
			val end = cal.timeInMillis
			
			return DateRange(start, end)
		}

		/**
		 * @return [DateRange] from first day of week until last day of week (in millis)
		 */
		fun week(millis: Long): DateRange {
			return DateModule.getWeek(millis).let { week ->
				DateRange(week.firstDay, week.lastDay)
			}
		}

		/**
		 * Custom date range
		 */
		fun of(from: Long, to: Long): DateRange {
			return DateRange(from, to)
		}
	}
	
}