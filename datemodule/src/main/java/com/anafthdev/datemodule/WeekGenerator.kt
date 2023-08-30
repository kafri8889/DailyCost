package com.anafthdev.datemodule

import com.anafthdev.datemodule.model.Week
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.Month
import java.util.Calendar

@Deprecated("Unstable")
class WeekGenerator() {

	private val currentCalendar = Calendar.getInstance()
	
	private val weeksOfPrevMonth = arrayListOf<Week>()
	private val weeksOfCurrentMonth = arrayListOf<Week>()
	private val weeksOfNextMonth = arrayListOf<Week>()
	
	private val _weeksOfPrevMonthFlow = MutableStateFlow(emptyList<Week>())
	private val _weeksOfCurrentMonthFlow = MutableStateFlow(emptyList<Week>())
	private val _weeksOfNextMonthFlow = MutableStateFlow(emptyList<Week>())
	
	val weeksOfPrevMonthFlow: StateFlow<List<Week>> = _weeksOfPrevMonthFlow
	val weeksOfCurrentMonthFlow: StateFlow<List<Week>> = _weeksOfCurrentMonthFlow
	val weeksOfNextMonthFlow: StateFlow<List<Week>> = _weeksOfNextMonthFlow
	
	init {
		getWeeks()
	}
	
	private fun getWeeks(month: Month? = null) {
		val currentMonth = month ?: DateModule.calendarMonthToJavaTimeMonth(currentCalendar.get(Calendar.MONTH))!!
		
		val prevMonth = DateModule.previousMonthOf(currentMonth)
		val nextMonth = DateModule.nextMonthOf(currentMonth)
		
		val prevMonthCalendar = Calendar.getInstance().apply {
			set(Calendar.MONTH, DateModule.javaTimeMonthToCalendarMonth(prevMonth))
			set(Calendar.DAY_OF_MONTH, 1)
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)
			set(Calendar.MILLISECOND, 1)
		}
		
		val nextMonthCalendar = Calendar.getInstance().apply {
			set(Calendar.MONTH, DateModule.javaTimeMonthToCalendarMonth(nextMonth))
			set(Calendar.DAY_OF_MONTH, nextMonth.maxLength())
			set(Calendar.HOUR_OF_DAY, 23)
			set(Calendar.MINUTE, 59)
			set(Calendar.SECOND, 59)
			set(Calendar.MILLISECOND, 998)
		}
		
		val weeks = DateModule.getAvailableWeeks(prevMonthCalendar.timeInMillis, nextMonthCalendar.timeInMillis)
		
		val groupedWeeks = weeks.groupBy { it.month }
		
		groupedWeeks.forEach { (month, values) ->
			when (DateModule.calendarMonthToJavaTimeMonth(month)) {
				prevMonth -> weeksOfPrevMonth.apply {
					clear()
					addAll(values)
					
					_weeksOfPrevMonthFlow.update { values }
				}
				currentMonth -> weeksOfCurrentMonth.apply {
					clear()
					addAll(values)
					
					_weeksOfCurrentMonthFlow.update { values }
				}
				nextMonth -> weeksOfNextMonth.apply {
					clear()
					addAll(values)
					
					_weeksOfNextMonthFlow.update { values }
				}
				else -> {}
			}
		}
	}
	
	fun setTimeInMillis(millis: Long) {
		if (DateModule.inSameMonth(currentCalendar.timeInMillis, millis)) return
		
		currentCalendar.timeInMillis = millis
		getWeeks()
	}
	
	fun move(month: Month) {
		getWeeks(month)
	}
	
}