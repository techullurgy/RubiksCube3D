package com.techullurgy.rubikscube.presentation.game.core.modifier

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import com.techullurgy.rubikscube.presentation.game.core.components.Cubie
import com.techullurgy.rubikscube.presentation.game.core.models.TurnAxis
import com.techullurgy.rubikscube.presentation.game.core.renderer.GameRenderer
import com.techullurgy.rubikscube.presentation.game.core.states.INITIAL_EYE
import com.techullurgy.rubikscube.presentation.game.utils.SphericalCameraController
import io.github.erkko68.filament.compose.FilamentViewState
import io.github.erkko68.filament.compose.scene.CameraState
import io.github.erkko68.filament.compose.scene.Viewpoint
import io.github.erkko68.filament.utils.Float3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalAtomicApi::class)
internal class GameGestureController(
    private val renderer: GameRenderer,
    private val filamentViewState: FilamentViewState,
    private val cameraState: CameraState,
    private val cameraController: SphericalCameraController,
    private val scope: CoroutineScope,
) {
    private val dragMode = AtomicReference(DragMode.Undecided)
    val startPosition = AtomicReference(Offset.Unspecified)
    val turnDecided = AtomicBoolean(false)
    val orientation = AtomicReference<Orientation?>(null)
    val touchedCube = AtomicReference< Cubie?>(null)
    val touchedFaceNormal = AtomicReference<Float3?>(null)

    fun onDragStart(startOffset: Offset) {
        startPosition.store(startOffset)
        filamentViewState.pick(
            x = startOffset.x.roundToInt(),
            y = startOffset.y.roundToInt()
        ) { res ->
            if(res.renderable == 0) {
                dragMode.store(DragMode.Camera)
            } else {
                touchedCube.store(renderer.cubes.first { c -> c.extractFaceNormalIfExists(res.renderable) != null })
                touchedFaceNormal.store(touchedCube.load()!!.extractFaceNormalIfExists(res.renderable)!!)
                dragMode.store(DragMode.Face)
            }
        }
    }

    fun onDragEndOrCancel() {
        when(dragMode.load()) {
            DragMode.Camera -> {
                scope.launch {
                    cameraState.animateTo(
                        viewpoint = Viewpoint(
                            eye = INITIAL_EYE,
                            target = cameraState.target
                        )
                    )
                    cameraController.updateCameraState(cameraState)
                }
            }
            DragMode.Face -> {
                scope.launch {
                    renderer.settleMove()
                }
            }
            DragMode.Undecided -> {}
        }

        dragMode.store(DragMode.Undecided)
        startPosition.store(Offset.Unspecified)
        orientation.store(null)
        touchedCube.store(null)
        touchedFaceNormal.store(null)
        turnDecided.store(false)
    }

    fun onDrag(
        change: PointerInputChange,
        dragAmount: Offset
    ) {
        when(dragMode.load()) {
            DragMode.Camera -> {
                change.consume()
                cameraController.orbit(dragAmount.x, dragAmount.y)
            }
            DragMode.Face -> {
                change.consume()

                val orientation = orientation.load() ?: run {
                    val xAbs = abs(dragAmount.x)
                    val yAbs = abs(dragAmount.y)
                    if(xAbs > yAbs) {
                        Orientation.Horizontal
                    } else {
                        Orientation.Vertical
                    }
                }.also {
                    orientation.store(it)
                }

                val cube = touchedCube.load()!!
                val faceNormal = touchedFaceNormal.load()!!


                if(turnDecided.load().not()) {
                    turnDecided.store(true)
                    determineTurnOnGesture(cube, faceNormal, orientation)
                }

                val distance = change.position - startPosition.load()
                objectMoves(distance, orientation)
            }
            DragMode.Undecided -> return
        }
    }

    private fun determineTurnOnGesture(
        cube: Cubie,
        faceNormal: Float3,
        orientation: Orientation
    ) {
        when {
            faceNormal.z > 0f -> {
                // Front Face Hit (Y-V, Z-H)
                when(orientation) {
                    Orientation.Vertical -> {
                        renderer.beginMove(TurnAxis.Y, cube.y)
                    }
                    Orientation.Horizontal -> {
                        renderer.beginMove(TurnAxis.Z, cube.z)
                    }
                }
            }
            faceNormal.y > 0f -> {
                // Top Face Hit (X-V, Y-H)
                when(orientation) {
                    Orientation.Vertical -> {
                        renderer.beginMove(TurnAxis.X, cube.x)
                    }
                    Orientation.Horizontal -> {
                        renderer.beginMove(TurnAxis.Y, cube.y)
                    }
                }
            }
            faceNormal.x > 0f -> {
                // Right Face Hit (Z-V, X-H)
                when(orientation) {
                    Orientation.Vertical -> {
                        renderer.beginMove(TurnAxis.Z, cube.z)
                    }
                    Orientation.Horizontal -> {
                        renderer.beginMove(TurnAxis.X, cube.x)
                    }
                }
            }
        }
    }

    private fun objectMoves(
        distance: Offset,
        orientation: Orientation,
    ) {
        when(orientation) {
            Orientation.Vertical -> {
                val angle = ((-distance.y) / 10f)
                scope.launch {
                    renderer.scrubMove(angle)
                }
            }

            Orientation.Horizontal -> {
                val angle = (distance.x / 10f)
                scope.launch {
                    renderer.scrubMove(angle)
                }
            }
        }

        /*

        (X/Y/Z) ====> Positive(Clockwise) / Negative(AntiClockwise)

        Front face Hit
            Vertical Drag
                Up Direction = -Y
                Down Direction = Y
            Horizontal Drag
                Left Direction = -Z
                Right Direction = Z

        Top face Hit
            Vertical Drag
                Up Direction = -X
                Down Direction = X
            Horizontal Drag
                Left Direction = -Y
                Right Direction = Y

        Right face Hit
            Vertical Drag
                Up Direction = -Z
                Down Direction = Z
            Horizontal Drag
                Left Direction = -X
                Right Direction = X
        */


    }
}

private enum class DragMode {
    Undecided,
    Camera,
    Face
}