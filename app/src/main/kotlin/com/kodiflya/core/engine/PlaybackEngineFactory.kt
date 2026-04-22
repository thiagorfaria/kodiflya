package com.kodiflya.core.engine

import com.kodiflya.core.plugin.AlgorithmPlugin
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CoroutineScope

@AssistedFactory
interface PlaybackEngineFactory {
    fun create(plugin: AlgorithmPlugin, scope: CoroutineScope): PlaybackEngine
}
