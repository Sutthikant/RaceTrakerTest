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

class RaceParticipantBoundary {
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

    @Test
    fun raceNotStarted_ProgressRemainsZero() = runTest {
        assertEquals(0, raceParticipant1.currentProgress)
        assertEquals(0, raceParticipant2.currentProgress)

        assertFalse(raceParticipant1.currentProgress > 0)
        assertFalse(raceParticipant2.currentProgress > 0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun raceCompletesExactlyAtMaxProgress() = runTest {
        launch { raceParticipant1.run() }
        launch { raceParticipant2.run() }

        while (raceParticipant1.currentProgress < 99 || raceParticipant2.currentProgress < 99) {
            advanceTimeBy(raceParticipant1.progressDelayMillis)
            runCurrent()
        }

        advanceTimeBy(raceParticipant1.progressDelayMillis)
        runCurrent()

        assertEquals(100, raceParticipant1.currentProgress)
        assertEquals(100, raceParticipant2.currentProgress)

        // Ensure progress does not exceed 100.
        assertFalse(raceParticipant1.currentProgress > 100)
        assertFalse(raceParticipant2.currentProgress > 100)
    }
}