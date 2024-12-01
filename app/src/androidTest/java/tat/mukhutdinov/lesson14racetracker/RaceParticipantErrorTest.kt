package tat.mukhutdinov.lesson14racetracker

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tat.mukhutdinov.lesson14racetracker.ui.RaceParticipant

class RaceParticipantErrorTest {
    private val raceParticipant1 = RaceParticipant(
        name = "player1",
        maxProgress = 100,
        progressDelayMillis = 5000L,
        initialProgress = 0,
        progressIncrement = 1
    )

    private val raceParticipant2 = RaceParticipant(
        name = "player2",
        maxProgress = 100,
        progressDelayMillis = 5000L,
        initialProgress = 0,
        progressIncrement = 2
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun progressExceedsMax_ClampedToMax() = runTest {
        // Run raceParticipant1 and raceParticipant2 until progress approaches the maximum.
        launch { raceParticipant1.run() }
        launch { raceParticipant2.run() }

        // Simulate progression close to the max (advance in small increments).
        while (raceParticipant1.currentProgress < 100 || raceParticipant2.currentProgress < 100) {
            advanceTimeBy(raceParticipant1.progressDelayMillis)
            runCurrent()

            // Break once both reach 100 to prevent overshooting.
            if (raceParticipant1.currentProgress >= 100 && raceParticipant2.currentProgress >= 100) {
                break
            }
        }

        // Assert progress is clamped at max (100).
        assertEquals(100, raceParticipant1.currentProgress)
        assertEquals(100, raceParticipant2.currentProgress)

        // Ensure progress is not greater than 100.
        assertFalse(raceParticipant1.currentProgress > 100)
        assertFalse(raceParticipant2.currentProgress > 100)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun resetWhileRunning_ResetsProgress() = runTest {
        launch { raceParticipant1.run() }
        advanceTimeBy(raceParticipant1.progressDelayMillis)
        raceParticipant1.reset()

        assertEquals(0, raceParticipant1.currentProgress)
    }
}