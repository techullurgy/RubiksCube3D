package com.techullurgy.rubikscube.presentation.game.core.modifier

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.techullurgy.rubikscube.presentation.game.core.renderer.GameRenderer
import com.techullurgy.rubikscube.presentation.game.utils.SphericalCameraController
import io.github.erkko68.filament.compose.FilamentViewState
import io.github.erkko68.filament.compose.scene.CameraState
import kotlinx.coroutines.CoroutineScope

internal fun Modifier.gameGestureModifier(
    renderer: GameRenderer,
    cameraState: CameraState,
    cameraController: SphericalCameraController,
    scope: CoroutineScope,
    filamentViewState: FilamentViewState
): Modifier {

    return this then Modifier
        .pointerInput(Unit) {
            val controller = GameGestureController(
                renderer,
                filamentViewState,
                cameraState,
                cameraController,
                scope
            )

            detectDragGestures(
                onDragStart = controller::onDragStart,
                onDragEnd = controller::onDragEndOrCancel,
                onDragCancel = controller::onDragEndOrCancel,
                onDrag = controller::onDrag
            )
        }
}