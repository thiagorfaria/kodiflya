package com.kodiflya.ui.icons

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AlgorithmIconSource {
    data class Vector(val icon: ImageVector) : AlgorithmIconSource()
    data class Local(@DrawableRes val resId: Int) : AlgorithmIconSource()
    // data class Remote(val url: String) : AlgorithmIconSource()  // v2
}
