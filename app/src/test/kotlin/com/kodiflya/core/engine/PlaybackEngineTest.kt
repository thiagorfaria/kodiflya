package com.kodiflya.core.engine

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.PlaybackStatus
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VisualizationStep
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaybackEngineTest {

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val fakeInput = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
    private val fakeSteps = (0..9).map { i ->
        VisualizationStep.Sort(
            values = intArrayOf(i, i + 1, i + 2),
            comparing = setOf(i % 3),
            swapping = emptySet(),
            sorted = emptySet(),
            pivot = null,
            metrics = SortMetrics(i.toLong(), 0L, i.toLong()),
        )
    }

    private val fakePlugin = object : AlgorithmPlugin {
        override val id = "fake"
        override val displayName = "Fake"
        override val category = Category.SORTING
        override val order = 0
        override val complexity = Complexity(BigO.O_1, BigO.O_1, BigO.O_1, BigO.O_1)
        override val metricLabels = listOf(MetricLabel("Test", ColorRole.NEUTRAL))
        override fun initialData(): AlgorithmInput = fakeInput
        override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> = fakeSteps.asSequence()
    }

    private lateinit var engine: PlaybackEngine

    @BeforeEach
    fun setUp() {
        engine = PlaybackEngine(fakePlugin, testScope)
    }

    @Test
    fun `initial state is IDLE with null currentStep`() {
        assertEquals(PlaybackStatus.IDLE, engine.state.value.playbackStatus)
        assertNull(engine.state.value.currentStep)
    }

    @Test
    fun `play transitions to PLAYING and advances cursor`() = testScope.runTest {
        engine.play()
        advanceTimeBy(401) // let the first step delay elapse (400ms default)

        assertEquals(PlaybackStatus.PLAYING, engine.state.value.playbackStatus)
        assertEquals(1, engine.state.value.stepIndex)
    }

    @Test
    fun `pause stops cursor advancement`() = testScope.runTest {
        engine.play()
        advanceTimeBy(800) // 2 steps at 400ms each
        engine.pause()
        val stoppedAt = engine.state.value.stepIndex

        advanceTimeBy(1200) // would advance 3 more steps if playing
        assertEquals(stoppedAt, engine.state.value.stepIndex)
        assertEquals(PlaybackStatus.PAUSED, engine.state.value.playbackStatus)
    }

    @Test
    fun `resume after pause continues from the paused step`() = testScope.runTest {
        engine.play()
        advanceTimeBy(800) // 3 steps processed at 400ms each (steps 0,1,2), cursor at 3
        engine.pause()
        val pausedIndex = engine.state.value.stepIndex // = 2

        engine.play() // resume from cursor=3
        // advanceTimeBy(1): lets the coroutine start and process step 3, but the
        // 400ms delay has not yet elapsed so no additional step runs
        advanceTimeBy(1)
        assertEquals(pausedIndex + 1, engine.state.value.stepIndex)
    }

    @Test
    fun `reset returns cursor to 0 and status to IDLE`() = testScope.runTest {
        engine.play()
        advanceTimeBy(1600) // 4 steps
        engine.reset()

        assertEquals(0, engine.state.value.stepIndex)
        assertEquals(PlaybackStatus.IDLE, engine.state.value.playbackStatus)
        assertNull(engine.state.value.currentStep)
    }

    @Test
    fun `speed change reduces step delay on next iteration`() = testScope.runTest {
        engine.setSpeed(8f) // 400/8 = 50ms per step
        engine.play()
        advanceTimeBy(51) // enough for one step at 50ms

        assertEquals(PlaybackStatus.PLAYING, engine.state.value.playbackStatus)
        assertEquals(1, engine.state.value.stepIndex)
    }

    @Test
    fun `play completes and sets status to DONE after all steps`() = testScope.runTest {
        engine.setSpeed(8f) // 50ms per step; 10 steps = 500ms total
        engine.play()
        advanceTimeBy(600)

        assertEquals(PlaybackStatus.DONE, engine.state.value.playbackStatus)
    }

    @Test
    fun `play is idempotent - calling play while playing does nothing`() = testScope.runTest {
        engine.play()
        advanceTimeBy(401)
        val indexAfterFirst = engine.state.value.stepIndex

        engine.play() // should not restart
        assertEquals(indexAfterFirst, engine.state.value.stepIndex)
    }
}
