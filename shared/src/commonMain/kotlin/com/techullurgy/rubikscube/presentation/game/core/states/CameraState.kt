package com.techullurgy.rubikscube.presentation.game.core.states

import androidx.compose.runtime.Composable
import io.github.erkko68.filament.compose.scene.CameraState
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.compose.scene.Projection
import io.github.erkko68.filament.compose.scene.rememberCameraState

internal val INITIAL_EYE = Position(5f, 4f, 10f)

@Composable
internal fun rememberInitialCameraState(): CameraState {
    val cameraState = rememberCameraState(
        initialEye        = INITIAL_EYE,
        initialTarget     = Position(0f, 0f, 0f),
        initialProjection = Projection.Perspective(fovDegrees = 45.0),
    )

    return cameraState
}