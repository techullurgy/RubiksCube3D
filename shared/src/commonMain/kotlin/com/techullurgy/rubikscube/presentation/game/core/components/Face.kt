package com.techullurgy.rubikscube.presentation.game.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.techullurgy.rubikscube.presentation.game.core.base.OneSidedPlane
import io.github.erkko68.filament.Entity
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.scene.LinearColor
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.compose.scene.Rotation
import io.github.erkko68.filament.utils.Float3
import io.github.erkko68.filament.utils.Quaternion
import io.github.erkko68.filament.utils.radians
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

internal class Face(
    normal: Float3,
    val color: LinearColor
) {
    var normal: Float3 by mutableStateOf(normal)
        private set

    var entity: Entity? = null
        private set

    fun turnX(angle: Float) {
        val angle = radians(angle)
        val temp = Float3()
        temp.x = round(normal.x)
        temp.y = round(normal.y * cos(angle) - normal.z * sin(angle))
        temp.z = round(normal.y * sin(angle) + normal.z * cos(angle))
        normal = temp
    }

    fun turnY(angle: Float) {
        val angle = radians(angle)
        val temp = Float3()
        temp.x = round(normal.x * cos(angle) - normal.z * sin(angle))
        temp.y = round(normal.y)
        temp.z = round(normal.x * sin(angle) + normal.z * cos(angle))
        normal = temp
    }

    fun turnZ(angle: Float) {
        val angle = radians(angle)
        val temp = Float3()
        temp.x = round(normal.x * cos(angle) - normal.y * sin(angle))
        temp.y = round(normal.x * sin(angle) + normal.y * cos(angle))
        temp.z = round(normal.z)
        normal = temp
    }

    @Composable
    context(scope: FilamentSceneScope)
    fun Draw() {
        val position = Position(normal * 0.5f)

        val rotation = if(abs(normal.x) > 0f) {
            Rotation(Quaternion.fromEuler(Float3(0f, normal.x * 90f, 0f)))
        } else if(abs(normal.y) > 0f) {
            Rotation(Quaternion.fromEuler(Float3((normal.y * 90f) + 180f, 0f, 0f)))
        } else if(normal.z > 0f) {
            Rotation.Identity
        } else {
            Rotation(Quaternion.fromEuler(Float3(0f, 180f, 0f)))
        }

        scope.OneSidedPlane(
            color = color,
            position = position,
            rotation = rotation
        ) {
            entity = it
        }
    }
}