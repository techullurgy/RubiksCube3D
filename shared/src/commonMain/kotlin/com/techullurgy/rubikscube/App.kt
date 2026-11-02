package com.techullurgy.rubikscube

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Grid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techullurgy.rubikscube.presentation.screens.GameScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Grid(
            config = {
                row(1f)
                column(1f)
            }
        ) {
            Box(
                modifier = Modifier
            ) {
                GameScreen()
            }
        }
    }
}