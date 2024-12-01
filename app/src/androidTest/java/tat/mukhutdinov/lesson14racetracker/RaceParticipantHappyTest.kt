package tat.mukhutdinov.lesson14racetracker

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tat.mukhutdinov.lesson14racetracker.ui.RaceParticipant

class RaceParticipantHappyTest {
    private val raceParticipant1 = RaceParticipant(
        name = "player1",
        maxProgress = 100,
        progressDelayMillis = 5000L,
        initialProgress = 0,
        progressIncrement = 1
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun raceStarted_ProgressUpdated() = runTest {
        val expectedProgress1 = 1
        val expectedProgress2 = 2

        launch { raceParticipant1.run() }

        advanceTimeBy(raceParticipant1.progressDelayMillis)
        runCurrent()

        assertEquals(expectedProgress1, raceParticipant1.currentProgress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun raceFinished_ProgressUpdated() = runTest {
        launch { raceParticipant1.run() }

        advanceUntilIdle()

        assertEquals(100, raceParticipant1.currentProgress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun raceResetCorrectly() = runTest {
        launch { raceParticipant1.run() }

        advanceUntilIdle()

        raceParticipant1.reset()

        assertEquals(0, raceParticipant1.currentProgress)
    }

}