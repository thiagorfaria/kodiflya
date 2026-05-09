package com.kodiflya.ui.component

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import com.kodiflya.core.plugin.PlaybackStatus
import com.kodiflya.ui.theme.KodiflyaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ControlsRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // region — Primary button icon visibility

    @Test
    fun whenStatusIsPlaying_pauseIconIsShown() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PLAYING,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Pause").assertIsDisplayed()
    }

    @Test
    fun whenStatusIsPaused_playIconIsShown() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PAUSED,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Play").assertIsDisplayed()
    }

    @Test
    fun whenStatusIsIdle_playIconIsShown() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Play").assertIsDisplayed()
    }

    @Test
    fun whenStatusIsDone_replayIconIsShown() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.DONE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Replay").assertIsDisplayed()
    }

    // endregion

    // region — Primary button callback routing

    @Test
    fun whenStatusIsIdle_tappingPrimaryButtonInvokesOnPlay() {
        var playCalled = false
        var pauseCalled = false
        var replayCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 1f,
                    onPlay = { playCalled = true },
                    onPause = { pauseCalled = true },
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = { replayCalled = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Play").performClick()

        assertTrue("onPlay should have been called", playCalled)
        assertFalse("onPause must not be called in IDLE state", pauseCalled)
        assertFalse("onReplay must not be called in IDLE state", replayCalled)
    }

    @Test
    fun whenStatusIsPaused_tappingPrimaryButtonInvokesOnPlay() {
        var playCalled = false
        var pauseCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PAUSED,
                    speedIndex = 1f,
                    onPlay = { playCalled = true },
                    onPause = { pauseCalled = true },
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Play").performClick()

        assertTrue("onPlay should have been called in PAUSED state", playCalled)
        assertFalse("onPause must not be called in PAUSED state", pauseCalled)
    }

    @Test
    fun whenStatusIsPlaying_tappingPrimaryButtonInvokesOnPause() {
        var playCalled = false
        var pauseCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PLAYING,
                    speedIndex = 1f,
                    onPlay = { playCalled = true },
                    onPause = { pauseCalled = true },
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Pause").performClick()

        assertTrue("onPause should have been called in PLAYING state", pauseCalled)
        assertFalse("onPlay must not be called in PLAYING state", playCalled)
    }

    @Test
    fun whenStatusIsDone_tappingPrimaryButtonInvokesOnReplay() {
        var replayCalled = false
        var playCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.DONE,
                    speedIndex = 1f,
                    onPlay = { playCalled = true },
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = { replayCalled = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Replay").performClick()

        assertTrue("onReplay should have been called in DONE state", replayCalled)
        assertFalse("onPlay must not be called in DONE state", playCalled)
    }

    // endregion

    // region — Reset button enabled/disabled behaviour

    @Test
    fun whenStatusIsIdle_tappingResetDoesNotInvokeOnReset() {
        var resetCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = { resetCalled = true },
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Reset").performClick()

        assertFalse("onReset must not fire when status is IDLE", resetCalled)
    }

    @Test
    fun whenStatusIsDone_tappingResetDoesNotInvokeOnReset() {
        var resetCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.DONE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = { resetCalled = true },
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Reset").performClick()

        assertFalse("onReset must not fire when status is DONE", resetCalled)
    }

    @Test
    fun whenStatusIsPlaying_tappingResetInvokesOnReset() {
        var resetCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PLAYING,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = { resetCalled = true },
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Reset").performClick()

        assertTrue("onReset should fire when status is PLAYING", resetCalled)
    }

    @Test
    fun whenStatusIsPaused_tappingResetInvokesOnReset() {
        var resetCalled = false

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.PAUSED,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = { resetCalled = true },
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Reset").performClick()

        assertTrue("onReset should fire when status is PAUSED", resetCalled)
    }

    // endregion

    // region — Speed label rendering

    @Test
    fun whenSpeedIndexIsOne_currentSpeedLabelShowsOneX() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        // "1×" appears only as the current-speed indicator at this index.
        composeTestRule.onNodeWithText("1×").assertIsDisplayed()
    }

    @Test
    fun whenSpeedIndexIsZero_currentSpeedLabelShowsHalfX() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 0f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        // "0.5×" appears twice: as a static range endpoint AND as the current-speed label.
        // onFirst() is sufficient — we need at least one visible node with this text.
        composeTestRule.onAllNodesWithText("0.5×").onFirst().assertIsDisplayed()
    }

    @Test
    fun whenSpeedIndexIsFour_currentSpeedLabelShowsEightX() {
        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 4f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = {},
                    onReplay = {},
                )
            }
        }

        // "8×" appears twice: as a static range endpoint AND as the current-speed label.
        composeTestRule.onAllNodesWithText("8×").onFirst().assertIsDisplayed()
    }

    // endregion

    // region — Speed slider onSpeedChange callback

    @Test
    fun settingSliderProgressInvokesOnSpeedChangeWithMappedValue() {
        var receivedValue = -1f

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 1f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = { receivedValue = it },
                    onReplay = {},
                )
            }
        }

        // SetProgress takes a value within the slider's valueRange (0f..4f).
        // Requesting 2f maps to speedLabels[2] = "2×" and exercises a non-endpoint step.
        composeTestRule
            .onNodeWithContentDescription("Speed slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { it(2f) }

        assertEquals(
            "onSpeedChange must receive the exact value passed to SetProgress",
            2f,
            receivedValue,
            0.01f,
        )
    }

    // endregion

    // region — Speed slider boundary values

    @Test
    fun settingSliderToMinValueInvokesOnSpeedChangeWithZero() {
        var receivedValue = -1f

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 2f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = { receivedValue = it },
                    onReplay = {},
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Speed slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { it(0f) }

        assertEquals(
            "onSpeedChange must receive 0f when slider is moved to minimum",
            0f,
            receivedValue,
            0.01f,
        )
    }

    @Test
    fun settingSliderToMaxValueInvokesOnSpeedChangeWithFour() {
        var receivedValue = -1f

        composeTestRule.setContent {
            KodiflyaTheme {
                ControlsRow(
                    playbackStatus = PlaybackStatus.IDLE,
                    speedIndex = 2f,
                    onPlay = {},
                    onPause = {},
                    onReset = {},
                    onSpeedChange = { receivedValue = it },
                    onReplay = {},
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Speed slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { it(4f) }

        assertEquals(
            "onSpeedChange must receive 4f when slider is moved to maximum",
            4f,
            receivedValue,
            0.01f,
        )
    }

    // endregion

}
