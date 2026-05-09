package com.kodiflya.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.theme.KodiflyaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AlgorithmChipRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Minimal stub — AlgorithmPlugin is pure Kotlin (zero Android deps per CLAUDE.md contract).
    // Implementing directly avoids MockK in instrumented tests.
    private fun stubPlugin(name: String, index: Int): AlgorithmPlugin = object : AlgorithmPlugin {
        override val id: String = "stub_$index"
        override val displayName: String = name
        override val category: Category = Category.SORTING
        override val order: Int = index
        override val complexity: Complexity = Complexity(BigO.O_N, BigO.O_N, BigO.O_N_SQUARED, BigO.O_1)
        override val metricLabels: List<MetricLabel> = emptyList()
        override fun initialData(): AlgorithmInput = AlgorithmInput.SortInput(intArrayOf())
        override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> = emptySequence()
    }

    private val threePlugins = listOf(
        stubPlugin("Bubble Sort", 0),
        stubPlugin("Insertion Sort", 1),
        stubPlugin("Merge Sort", 2),
    )

    // region — Chip presence

    @Test
    fun allPluginNamesAreRenderedAsChips() {
        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 0,
                    onSelect = {},
                )
            }
        }

        threePlugins.forEach { plugin ->
            composeTestRule.onNodeWithText(plugin.displayName).assertIsDisplayed()
        }
    }

    // endregion

    // region — Selected chip testTag is present at active index

    @Test
    fun chipAtActiveIndexHasExpectedTestTag() {
        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 1,
                    onSelect = {},
                )
            }
        }

        // The chip at index 1 must exist and be displayed.
        // Test tag "algorithm_chip_1" is only present at the active index position.
        composeTestRule.onNodeWithTag("algorithm_chip_1").assertIsDisplayed()
    }

    @Test
    fun chipAtNonActiveIndexHasExpectedTestTag() {
        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 0,
                    onSelect = {},
                )
            }
        }

        // Non-active chips are still rendered; their tags must be correct.
        composeTestRule.onNodeWithTag("algorithm_chip_2").assertIsDisplayed()
    }

    // endregion

    // region — onSelect callback with correct index

    @Test
    fun tappingFirstChipInvokesOnSelectWithIndexZero() {
        var selectedIndex = -1

        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 1,
                    onSelect = { selectedIndex = it },
                )
            }
        }

        composeTestRule.onNodeWithTag("algorithm_chip_0").performClick()

        assertEquals("onSelect must pass index 0 for the first chip", 0, selectedIndex)
    }

    @Test
    fun tappingSecondChipInvokesOnSelectWithIndexOne() {
        var selectedIndex = -1

        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 0,
                    onSelect = { selectedIndex = it },
                )
            }
        }

        composeTestRule.onNodeWithTag("algorithm_chip_1").performClick()

        assertEquals("onSelect must pass index 1 for the second chip", 1, selectedIndex)
    }

    @Test
    fun tappingThirdChipInvokesOnSelectWithIndexTwo() {
        var selectedIndex = -1

        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 0,
                    onSelect = { selectedIndex = it },
                )
            }
        }

        composeTestRule.onNodeWithTag("algorithm_chip_2").performClick()

        assertEquals("onSelect must pass index 2 for the third chip", 2, selectedIndex)
    }

    @Test
    fun tappingAlreadyActiveChipStillInvokesOnSelect() {
        var callCount = 0

        composeTestRule.setContent {
            KodiflyaTheme {
                AlgorithmChipRow(
                    plugins = threePlugins,
                    activeIndex = 0,
                    onSelect = { callCount++ },
                )
            }
        }

        // AlgorithmChipRow itself does not guard against re-selecting the active chip —
        // that deduplication is in SortingViewModel. The composable must still fire.
        composeTestRule.onNodeWithTag("algorithm_chip_0").performClick()

        assertEquals("onSelect must be invoked even when the tapped chip is already active", 1, callCount)
    }

    // endregion
}
