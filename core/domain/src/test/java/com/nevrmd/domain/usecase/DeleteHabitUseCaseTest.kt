package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteHabitUseCaseTest {

    private lateinit var repository: HabitRepository
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase

    @Before
    fun setUp() {
        repository = mockk()
        deleteHabitUseCase = DeleteHabitUseCase(repository)
    }

    @Test
    fun `when repository deletes successfully, returns Success`() = runTest {
        coEvery { repository.deleteHabitById("1") } returns Unit

        val result = deleteHabitUseCase("1")

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        coVerify(exactly = 1) { repository.deleteHabitById("1") }
    }

    @Test
    fun `when repository throws, returns Error with the exception`() = runTest {
        val exception = IllegalStateException("row locked")
        coEvery { repository.deleteHabitById("1") } throws exception

        val result = deleteHabitUseCase("1") as DataResult.Error

        assertThat(result.exception).isEqualTo(exception)
        assertThat(result.message).isEqualTo("row locked")
    }
}
