package com.techullurgy.rubikscube.presentation.game.core.renderer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import com.techullurgy.rubikscube.presentation.game.core.base.Group
import com.techullurgy.rubikscube.presentation.game.core.components.Cubie
import com.techullurgy.rubikscube.presentation.game.core.models.CubieRotator
import com.techullurgy.rubikscube.presentation.game.core.models.TurnAxis
import com.techullurgy.rubikscube.presentation.game.core.models.TurnPhase
import com.techullurgy.rubikscube.presentation.game.utils.rotate2D
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.scene.Rotation
import io.github.erkko68.filament.utils.Float2
import io.github.erkko68.filament.utils.Float3
import io.github.erkko68.filament.utils.Quaternion
import kotlin.math.abs
import kotlin.math.round

internal class GameRenderer {
    val cubes = (-1..1).flatMap { x ->
        (-1..1).flatMap { y ->
            (-1..1).map { z ->
                Cubie(Float3(x.toFloat(), y.toFloat(), z.toFloat()))
            }
        }
    }

    private var activeSlice: ActiveSlice? = null

    private val rotator = CubieRotator()

    fun beginMove(axis: TurnAxis, sliceIndex: Int): Boolean {
        prepareSlice(axis, sliceIndex)
        return true
    }

    suspend fun scrubMove(angleInDegrees: Float) {
        if (activeSlice == null) return
        val clamped = angleInDegrees.coerceIn(-90f, 90f)
        val targetPhase = if(clamped >= 0f) TurnPhase.Clockwise else TurnPhase.AntiClockwise
        val fraction = (abs(clamped) / 90f).coerceIn(0f, 1f)

        rotator.seekTo(fraction, targetPhase)
    }

    suspend fun settleMove(velocityDegreesPerSec: Float = 0f) {
        val currentTarget = rotator.targetState
        val progress = rotator.fraction

        // Commit if dragged past 40% or flung with high velocity
        val shouldCommit = progress > 0.4f || abs(velocityDegreesPerSec) > 300f

        if (shouldCommit && currentTarget != TurnPhase.Idle) {
            // Animate the rest of the way to +/- 90°
            rotator.animateTo(
                target = currentTarget,
                spec = tween(durationMillis = 400, easing = LinearEasing)
            )
            val dir = if (currentTarget == TurnPhase.Clockwise) 1 else -1
            commitAndResetMove(dir)
        } else {
            // Snap back to Idle (0°) without mutating cube normals
            rotator.animateTo(
                target = TurnPhase.Idle,
                spec = tween(durationMillis = 200, easing = LinearEasing)
            )
            commitAndResetMove(dir = 0)
        }
    }

    @Composable
    context(scope: FilamentSceneScope)
    fun Render() = with(scope) {
        val transition = rememberTransition(rotator.rotatorTransition, label = "RubiksSliceTransition")

        // Linear interpolation during seekTo/animateTo so the angle matches fraction * 90f
        val animatedAngle by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 700, easing = LinearEasing) },
            label = "SliceAngle"
        ) { phase ->
            phase.targetAngle
        }

        val movableCubes = remember(cubes) {
            cubes.associateWith { cube ->
                movableContentOf {
                    cube.Draw()
                }
            }
        }

        val slice = activeSlice
        val activeSet = remember(slice) { slice?.affectedCubes?.toSet() ?: emptySet() }

        // 1. Static cubes (the 18 cubes not currently moving)
        cubes.forEach { cube ->
            if (cube !in activeSet) {
                movableCubes[cube]?.invoke()
            }
        }

        // 2. Active slice (the 9 moving cubes rotated together by a single parent Group!)
        if (slice != null) {
            val sliceRotation = when (slice.axis) {
                TurnAxis.X -> Rotation(Quaternion.fromEuler(Float3(animatedAngle, 0f, 0f)))
                TurnAxis.Y -> Rotation(Quaternion.fromEuler(Float3(0f, animatedAngle, 0f)))
                TurnAxis.Z -> Rotation(Quaternion.fromEuler(Float3(0f, 0f, animatedAngle)))
            }

            Group(
                rotation = sliceRotation
            ) {
                slice.affectedCubes.forEach { cube ->
                    movableCubes[cube]?.invoke()
                }
            }
        }
    }

    private suspend fun commitAndResetMove(dir: Int) {
        val slice = activeSlice ?: return
        val finalAngle = dir * 90f

        if(finalAngle != 0f) {
            Snapshot.withMutableSnapshot {
                slice.affectedCubes.forEach { cube ->
                    val snappedPos = when (slice.axis) {
                        TurnAxis.X -> {
                            val p = rotate2D(finalAngle, Float2(cube.y.toFloat(), cube.z.toFloat()))
                            Float3(round(cube.x.toFloat()), round(p.x), round(p.y))
                        }
                        TurnAxis.Y -> {
                            // Y angle should flipped, because,
                            // In World coordinates, Up is positive,
                            // In Android, Up is negative
                            val p = rotate2D(-finalAngle, Float2(cube.x.toFloat(), cube.z.toFloat()))
                            Float3(round(p.x), round(cube.y.toFloat()), round(p.y))
                        }
                        TurnAxis.Z -> {
                            val p = rotate2D(finalAngle, Float2(cube.x.toFloat(), cube.y.toFloat()))
                            Float3(round(p.x), round(p.y), round(cube.z.toFloat()))
                        }
                    }

                    cube.update(snappedPos)
                    when (slice.axis) {
                        TurnAxis.X -> cube.turnFacesX(dir)
                        // Y angle should flipped, because,
                        // In World coordinates, Up is positive,
                        // In Android, Up is negative
                        TurnAxis.Y -> cube.turnFacesY(-dir)
                        TurnAxis.Z -> cube.turnFacesZ(dir)
                    }
                }
            }
        }

        // Instantly snap transition back to Idle (0°) now that the grid has updated
        rotator.snapTo(TurnPhase.Idle)
        activeSlice = null
    }

    private fun prepareSlice(axis: TurnAxis, index: Int) {
        val affected = cubes.filter { cube ->
            when (axis) {
                TurnAxis.X -> cube.x == index
                TurnAxis.Y -> cube.y == index
                TurnAxis.Z -> cube.z == index
            }
        }
        activeSlice = ActiveSlice(
            axis = axis,
            sliceIndex = index,
            affectedCubes = affected,
        )
    }
}

private data class ActiveSlice(
    val axis: TurnAxis,
    val sliceIndex: Int,
    val affectedCubes: List<Cubie>
)