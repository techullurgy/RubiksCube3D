package com.techullurgy.rubikscube.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.techullurgy.rubikscube.presentation.core.AndroidGameController
import com.techullurgy.rubikscube.presentation.core.GameController
import com.techullurgy.rubikscube.presentation.views.RubiksCubeGameView

@Composable
actual fun GameView(
    controller: GameController,
    modifier: Modifier
) {
    val androidController = controller as AndroidGameController

    AndroidView(
        modifier = modifier,
        factory = { context ->
            RubiksCubeGameView(context).also(androidController::attach)
        },
        onReset = {
            androidController.attach(it)
        },
        onRelease = {
            androidController.detach()
        },
    )
}