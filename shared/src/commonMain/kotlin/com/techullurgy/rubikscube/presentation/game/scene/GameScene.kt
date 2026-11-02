package com.techullurgy.rubikscube.presentation.game.scene

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.techullurgy.rubikscube.presentation.game.core.modifier.gameGestureModifier
import com.techullurgy.rubikscube.presentation.game.core.renderer.GameRenderer
import com.techullurgy.rubikscube.presentation.game.core.states.rememberInitialCameraState
import com.techullurgy.rubikscube.presentation.game.utils.SphericalCameraController
import io.github.erkko68.filament.compose.FilamentSceneView
import io.github.erkko68.filament.compose.rememberFilamentViewState

@Composable
internal fun GameScene(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val gameRenderer = remember { GameRenderer() }

    val cameraState = rememberInitialCameraState()

    val cameraController = remember(cameraState) {
        SphericalCameraController(cameraState)
    }

    val filamentViewState = rememberFilamentViewState()

    FilamentSceneView(
        modifier = modifier
            .gameGestureModifier(
                gameRenderer,
                cameraState,
                cameraController,
                scope,
                filamentViewState
            ),
        cameraState = cameraState,
        transparent = true,
        viewState = filamentViewState,
    ) {
        LightSetup()
        gameRenderer.Render()
    }
}
