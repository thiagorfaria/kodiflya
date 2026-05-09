package com.kodiflya.ui.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

private val black = SolidColor(Color.Black)
private const val STROKE_W = 2f

private fun nodes(svgPath: String) = PathParser().parsePathString(svgPath).toNodes()

private fun ImageVector.Builder.strokePath(data: String) = addPath(
    pathData = nodes(data),
    stroke = black,
    strokeLineWidth = STROKE_W,
    strokeLineCap = StrokeCap.Round,
    strokeLineJoin = StrokeJoin.Round,
)

private fun ImageVector.Builder.fillPath(data: String) = addPath(
    pathData = nodes(data),
    fill = black,
)

private fun ImageVector.Builder.fillStrokePath(data: String) = addPath(
    pathData = nodes(data),
    fill = black,
    stroke = black,
    strokeLineWidth = STROKE_W,
    strokeLineCap = StrokeCap.Round,
    strokeLineJoin = StrokeJoin.Round,
)

private fun navIcon(name: String, block: ImageVector.Builder.() -> Unit): ImageVector =
    ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply(block).build()

// SVG path data — shared between outlined and filled variants
private const val HOME_PATH =
    "M4 10.4L12 4l8 6.4V19a2 2 0 0 1-2 2h-3v-5.5a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1V21H6a2 2 0 0 1-2-2v-8.6z"

private const val SORT_BAR_SHORT =
    "M5.5 13 h1 a1.5 1.5 0 0 1 1.5 1.5 v4 a1.5 1.5 0 0 1 -1.5 1.5 h-1 a1.5 1.5 0 0 1 -1.5 -1.5 v-4 a1.5 1.5 0 0 1 1.5 -1.5 Z"
private const val SORT_BAR_MID =
    "M11.5 8 h1 a1.5 1.5 0 0 1 1.5 1.5 v9 a1.5 1.5 0 0 1 -1.5 1.5 h-1 a1.5 1.5 0 0 1 -1.5 -1.5 v-9 a1.5 1.5 0 0 1 1.5 -1.5 Z"
private const val SORT_BAR_TALL =
    "M17.5 3 h1 a1.5 1.5 0 0 1 1.5 1.5 v14 a1.5 1.5 0 0 1 -1.5 1.5 h-1 a1.5 1.5 0 0 1 -1.5 -1.5 v-14 a1.5 1.5 0 0 1 1.5 -1.5 Z"

// Circle paths: M cx cy  m -r 0  a r r 0 1 0 2r 0  a r r 0 1 0 -2r 0
private const val GRAPH_CIRCLE_L = "M 5 5 m -2.2 0 a 2.2 2.2 0 1 0 4.4 0 a 2.2 2.2 0 1 0 -4.4 0 Z"
private const val GRAPH_CIRCLE_R = "M 19 5 m -2.2 0 a 2.2 2.2 0 1 0 4.4 0 a 2.2 2.2 0 1 0 -4.4 0 Z"
private const val GRAPH_CIRCLE_B = "M 12 19 m -2.2 0 a 2.2 2.2 0 1 0 4.4 0 a 2.2 2.2 0 1 0 -4.4 0 Z"
private const val GRAPH_LINES    = "M7.2 5h9.6 M6.8 6.6l4.2 10.5 M17.2 6.6l-4.2 10.5"

private const val TREES_ROOT    = "M 12 4 m -2 0 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0 Z"
private const val TREES_MID_L   = "M 6 12 m -2 0 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0 Z"
private const val TREES_MID_R   = "M 18 12 m -2 0 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0 Z"
private const val TREES_LEAF_L  = "M 6 20 m -1.75 0 a 1.75 1.75 0 1 0 3.5 0 a 1.75 1.75 0 1 0 -3.5 0 Z"
private const val TREES_LEAF_R  = "M 18 20 m -1.75 0 a 1.75 1.75 0 1 0 3.5 0 a 1.75 1.75 0 1 0 -3.5 0 Z"
private const val TREES_BRANCHES = "M10.7 5.4L7.3 10.6 M13.3 5.4L16.7 10.6 M6 14v4 M18 14v4"

// Magnifying glass: circle lens + diagonal handle
private const val SEARCH_LENS   = "M 10 10 m -5 0 a 5 5 0 1 0 10 0 a 5 5 0 1 0 -10 0 Z"
private const val SEARCH_HANDLE = "M 13.5 13.5 L 19 19"

object NavigationIcons {

    val HomeOutlined: ImageVector by lazy {
        navIcon("nav.home.outlined") { strokePath(HOME_PATH) }
    }

    val HomeFilled: ImageVector by lazy {
        navIcon("nav.home.filled") { fillStrokePath(HOME_PATH) }
    }

    val SortOutlined: ImageVector by lazy {
        navIcon("nav.sort.outlined") {
            strokePath(SORT_BAR_SHORT)
            strokePath(SORT_BAR_MID)
            strokePath(SORT_BAR_TALL)
        }
    }

    val SortFilled: ImageVector by lazy {
        navIcon("nav.sort.filled") {
            fillStrokePath(SORT_BAR_SHORT)
            fillStrokePath(SORT_BAR_MID)
            fillStrokePath(SORT_BAR_TALL)
        }
    }

    val GraphOutlined: ImageVector by lazy {
        navIcon("nav.graph.outlined") {
            strokePath(GRAPH_CIRCLE_L)
            strokePath(GRAPH_CIRCLE_R)
            strokePath(GRAPH_CIRCLE_B)
            strokePath(GRAPH_LINES)
        }
    }

    val GraphFilled: ImageVector by lazy {
        navIcon("nav.graph.filled") {
            fillPath(GRAPH_CIRCLE_L)
            fillPath(GRAPH_CIRCLE_R)
            fillPath(GRAPH_CIRCLE_B)
            strokePath(GRAPH_LINES)
        }
    }

    val TreesOutlined: ImageVector by lazy {
        navIcon("nav.trees.outlined") {
            strokePath(TREES_ROOT)
            strokePath(TREES_MID_L)
            strokePath(TREES_MID_R)
            strokePath(TREES_LEAF_L)
            strokePath(TREES_LEAF_R)
            strokePath(TREES_BRANCHES)
        }
    }

    val TreesFilled: ImageVector by lazy {
        navIcon("nav.trees.filled") {
            fillPath(TREES_ROOT)
            fillPath(TREES_MID_L)
            fillPath(TREES_MID_R)
            fillPath(TREES_LEAF_L)
            fillPath(TREES_LEAF_R)
            strokePath(TREES_BRANCHES)
        }
    }

    val SearchOutlined: ImageVector by lazy {
        navIcon("nav.search.outlined") {
            strokePath(SEARCH_LENS)
            strokePath(SEARCH_HANDLE)
        }
    }

    val SearchFilled: ImageVector by lazy {
        navIcon("nav.search.filled") {
            fillPath(SEARCH_LENS)
            strokePath(SEARCH_HANDLE)
        }
    }
}
