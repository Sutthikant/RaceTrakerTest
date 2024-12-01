package tat.mukhutdinov.lesson14racetracker

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tat.mukhutdinov.lesson14racetracker.ui.RaceParticipant
import kotlin.math.max

class RaceParticipantTest {
    private val raceParticipant = RaceParticipant(
        name = "Test",
        maxProgress = 100,
        progressDelayMillis = 5000L,
        initialProgress = 0,
        progressIncrement = 1
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun receStarted_ProgressUpdated() = runTest {
        val expectedProgress = 1
        launch {
            raceParticipant.run()
        }
        advanceTimeBy(raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun raceFinished_ProgressUpdated() = runTest {
        launch { raceParticipant.run() }
        advanceUntilIdle()
        assertEquals(100, raceParticipant.currentProgress)
    }

}