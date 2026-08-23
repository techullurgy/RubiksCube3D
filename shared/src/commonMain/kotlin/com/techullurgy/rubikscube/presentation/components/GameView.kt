package com.techullurgy.rubikscube.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.rubikscube.presentation.core.GameController

@Composable
expect fun GameView(
    controller: GameController,
    modifier: Modifier = Modifier
)