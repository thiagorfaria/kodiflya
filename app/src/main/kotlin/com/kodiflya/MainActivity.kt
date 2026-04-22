package com.kodiflya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kodiflya.ui.navigation.KodiflyaNavGraph
import com.kodiflya.ui.theme.KodiflyaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KodiflyaTheme {
                KodiflyaNavGraph()
            }
        }
    }
}
