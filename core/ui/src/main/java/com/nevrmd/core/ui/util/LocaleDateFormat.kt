package com.nevrmd.core.ui.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.Locale

fun DayOfWeek.shortDisplayName(locale: Locale = Locale.getDefault()): String =
    getDisplayName(TextStyle.SHORT, locale).uppercase(locale)

fun DayOfWeek.fullDisplayName(locale: Locale = Locale.getDefault()): String =
    getDisplayName(TextStyle.FULL, locale)

fun Month.displayName(locale: Locale = Locale.getDefault()): String =
    getDisplayName(TextStyle.FULL, locale)
