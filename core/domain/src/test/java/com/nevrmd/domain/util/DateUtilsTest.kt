package com.nevrmd.domain.util

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `getMonday returns same date when input is already Monday`() {
        val monday = LocalDate.parse("2024-01-01")

        assertThat(DateUtils.getMonday(monday)).isEqualTo(monday)
    }

    @Test
    fun `getMonday returns preceding Monday when input is mid-week`() {
        val wednesday = LocalDate.parse("2024-01-03")

        assertThat(DateUtils.getMonday(wednesday)).isEqualTo(LocalDate.parse("2024-01-01"))
    }

    @Test
    fun `getMonday returns preceding Monday when input is Sunday`() {
        val sunday = LocalDate.parse("2024-01-07")

        assertThat(DateUtils.getMonday(sunday)).isEqualTo(LocalDate.parse("2024-01-01"))
    }

    @Test
    fun `getWeekRange returns Monday through Sunday for a mid-week date`() {
        val wednesday = LocalDate.parse("2024-01-03")

        val (monday, sunday) = DateUtils.getWeekRange(wednesday)

        assertThat(monday).isEqualTo(LocalDate.parse("2024-01-01"))
        assertThat(sunday).isEqualTo(LocalDate.parse("2024-01-07"))
    }

    @Test
    fun `getMonthRange returns first and last day of a 31-day month`() {
        val midMonth = LocalDate.parse("2024-01-15")

        val (start, end) = DateUtils.getMonthRange(midMonth)

        assertThat(start).isEqualTo(LocalDate.parse("2024-01-01"))
        assertThat(end).isEqualTo(LocalDate.parse("2024-01-31"))
    }

    @Test
    fun `getMonthRange handles February in a leap year`() {
        val leapFebruary = LocalDate.parse("2024-02-10")

        val (start, end) = DateUtils.getMonthRange(leapFebruary)

        assertThat(start).isEqualTo(LocalDate.parse("2024-02-01"))
        assertThat(end).isEqualTo(LocalDate.parse("2024-02-29"))
    }

    @Test
    fun `getMonthRange handles year boundary in December`() {
        val december = LocalDate.parse("2023-12-25")

        val (start, end) = DateUtils.getMonthRange(december)

        assertThat(start).isEqualTo(LocalDate.parse("2023-12-01"))
        assertThat(end).isEqualTo(LocalDate.parse("2023-12-31"))
    }
}
