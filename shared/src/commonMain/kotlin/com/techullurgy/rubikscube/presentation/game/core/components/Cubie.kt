package com.techullurgy.rubikscube.presentation.game.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.techullurgy.rubikscube.presentation.game.core.base.Group
import io.github.erkko68.filament.Entity
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.scene.LinearColor
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.utils.Float3
import kotlin.math.roundToInt

internal class Cubie(
    position: Float3
) {
    var position by mutableStateOf(position)
        private set

    val x: Int get() = position.x.roundToInt()
    val y: Int get() = position.y.roundToInt()
    val z: Int get() = position.z.roundToInt()

    private val faces = listOf(
        Face(Float3(0f, 0f, 1f), LinearColor(1f, 0f, 0f)), // Red
        Face(Float3(0f, 0f, -1f), LinearColor(0f, 1f, 0f)), // Green
        Face(Float3(0f, 1f, 0f), LinearColor(0f, 0f, 1f)), // Blue
        Face(Float3(0f, -1f, 0f), LinearColor(1f, 1f, 0f)), // Yellow
        Face(Float3(1f, 0f, 0f), LinearColor(0f, 1f, 1f)), // Teal
        Face(Float3(-1f, 0f, 0f), LinearColor(1f, 0f, 1f)) // Magenta
    )

    fun extractFaceNormalIfExists(faceEntityId: Entity): Float3? = faces.find { it.entity == faceEntityId }?.normal

    fun turnFacesX(dir: Int) {
        faces.forEach { it.turnX(dir * 90f) }
    }

    fun turnFacesY(dir: Int) {
        faces.forEach { it.turnY(dir * 90f) }
    }

    fun turnFacesZ(dir: Int) {
        faces.forEach { it.turnZ(dir * 90f) }
    }

    fun update(pos: Float3) {
        position = pos.copy()
    }

    @Composable
    context(scope: FilamentSceneScope)
    fun Draw() = with(scope) {
        Group(
            position = Position(position)
        ) {
            faces.forEach {
                it.Draw()
            }
        }
    }
}