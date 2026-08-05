package com.nevrmd.domain.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

object DateUtils {

    fun getMonday(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.value
        return date.minus(DatePeriod(days = dayOfWeek - 1))
    }

    fun getWeekRange(date: LocalDate): Pair<LocalDate, LocalDate> {
        val monday = getMonday(date)
        val sunday = monday.plus(DatePeriod(days = 6))
        return monday to sunday
    }

    fun getMonthRange(date: LocalDate): Pair<LocalDate, LocalDate> {
        val firstDay = date.minus(DatePeriod(days = date.dayOfMonth - 1))
        val lastDay = firstDay.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
        return firstDay to lastDay
    }
}
